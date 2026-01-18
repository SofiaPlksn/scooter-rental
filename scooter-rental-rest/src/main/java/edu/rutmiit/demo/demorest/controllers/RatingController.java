package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.demorest.config.RabbitMQConfig;
import edu.rutmiit.demo.events.UserRatedEvent;
import grpc.demo.analytics.AnalyticsServiceGrpc;
import grpc.demo.analytics.UserRatingRequest;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class RatingController {

    private static final Logger log = LoggerFactory.getLogger(RatingController.class);

    private final AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;
    private final RabbitTemplate rabbitTemplate;

    public RatingController(AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub,
                            RabbitTemplate rabbitTemplate) {
        this.analyticsStub = analyticsStub;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/{id}/calculate-rating")
    public ResponseEntity<?> calculateUserRating(@PathVariable Long id) {
        log.info("Calculating rating for user: {}", id);

        try {
            // Синхронный вызов gRPC сервиса аналитики
            UserRatingRequest request = UserRatingRequest.newBuilder()
                    .setUserId(id)
                    .setCategory("RENTAL_BEHAVIOR")
                    .build();

            log.debug("Sending gRPC request for user: {}", id);
            var gRpcResponse = analyticsStub.calculateUserRating(request);

            // Создаем и публикуем событие в Fanout exchange
            UserRatedEvent event = new UserRatedEvent(
                    gRpcResponse.getUserId(),
                    gRpcResponse.getRatingScore(),
                    gRpcResponse.getVerdict()
            );

            // Для Fanout routingKey игнорируется, можно оставить пустым
            rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_ANALYTICS, "", event);

            var response = new RatingResponse(
                    gRpcResponse.getUserId(),
                    gRpcResponse.getRatingScore(),
                    gRpcResponse.getVerdict(),
                    "Rating calculated successfully"
            );

            log.info("Rating calculated successfully for user: {}, score: {}", id, gRpcResponse.getRatingScore());
            return ResponseEntity.ok(response);

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for user {}: {}", id, e.getStatus());
            return ResponseEntity.status(503)
                    .body(new ErrorResponse("Analytics service unavailable", -1));
        } catch (Exception e) {
            log.error("Unexpected error calculating rating for user {}: {}", id, e.getMessage());
            return ResponseEntity.status(500)
                    .body(new ErrorResponse("Internal server error", -1));
        }
    }

    // DTO классы
    public record RatingResponse(Long userId, Integer score, String verdict, String message) {}
    public record ErrorResponse(String error, Integer score) {}
}