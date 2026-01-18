package grpc.demo;

import grpc.demo.analytics.AnalyticsServiceGrpc;
import grpc.demo.analytics.UserRatingRequest;
import grpc.demo.analytics.UserRatingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Override
    public void calculateUserRating(UserRatingRequest request, StreamObserver<UserRatingResponse> responseObserver) {
        log.info("Calculating rating for user: {}, category: {}", request.getUserId(), request.getCategory());

        // Имитация сложных расчетов рейтинга пользователя
        int score = calculateUserRatingScore(request.getUserId());
        String verdict = getVerdict(score);

        UserRatingResponse response = UserRatingResponse.newBuilder()
                .setUserId(request.getUserId())
                .setRatingScore(score)
                .setVerdict(verdict)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Rating calculated - User: {}, Score: {}, Verdict: {}",
                request.getUserId(), score, verdict);
    }

    private int calculateUserRatingScore(long userId) {
        // Пока используем детерминированный "случайный" расчет для демонстрации
        return (int) ((userId * 31 + System.currentTimeMillis() % 100) % 100);
    }

    private String getVerdict(int score) {
        if (score >= 80) return "EXCELLENT";
        if (score >= 60) return "GOOD";
        if (score >= 40) return "AVERAGE";
        return "NEEDS_IMPROVEMENT";
    }
}