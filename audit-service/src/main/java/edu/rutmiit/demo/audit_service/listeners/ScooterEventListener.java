package edu.rutmiit.demo.audit_service.listeners;

import edu.rutmiit.demo.events.ScooterCreatedEvent;
import edu.rutmiit.demo.events.ScooterDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScooterEventListener {

    private static final Logger log = LoggerFactory.getLogger(ScooterEventListener.class);
    private final Set<Long> processedScooterCreations = ConcurrentHashMap.newKeySet();

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "scooter-created-audit-queue",
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.scooter-audit")
                            }
                    ),
                    exchange = @Exchange(name = "scooter-rental-exchange", type = "topic", durable = "true"),
                    key = "scooter.created"
            ),
            ackMode = "MANUAL"
    )
    public void handleScooterCreatedEvent(@Payload ScooterCreatedEvent event,
                                          Channel channel,
                                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        try {
            // Защита от дубликатов
            if (!processedScooterCreations.add(event.scooterId())) {
                log.warn("Audit: DUPLICATE event received for scooterId: {}. Already processed.", event.scooterId());
                channel.basicAck(deliveryTag, false); // Подтверждаем дубликат
                return;
            }

            log.info("Audit: A new scooter created");
            log.info("ID: {}", event.scooterId());
            log.info("Model: {}", event.model());
            log.info("SerialNumber: {}", event.serialNumber());

            // Подтверждаем успешную обработку
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Audit: Failed to process ScooterCreatedEvent for scooterId: {}. Sending to DLQ.",
                    event.scooterId(), e);
            processedScooterCreations.remove(event.scooterId()); // Удаляем из обработанных
            channel.basicNack(deliveryTag, false, false); // Не переотправляем
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "scooter-deleted-audit-queue",
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.scooter-audit")
                            }
                    ),
                    exchange = @Exchange(name = "scooter-rental-exchange", type = "topic", durable = "true"),
                    key = "scooter.deleted"
            ),
            ackMode = "MANUAL"
    )
    public void handleScooterDeletedEvent(@Payload ScooterDeletedEvent event,
                                          Channel channel,
                                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        try {
            log.info("Audit: Removed scooter");
            log.info("ID of the removed scooter: {}", event.scooterId());

            // Удаляем из множества обработанных созданий
            processedScooterCreations.remove(event.scooterId());

            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Audit: Failed to process ScooterDeletedEvent for scooterId: {}. Sending to DLQ.",
                    event.scooterId(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}