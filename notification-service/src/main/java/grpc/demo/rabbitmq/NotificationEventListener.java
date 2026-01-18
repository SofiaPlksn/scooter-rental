package grpc.demo.rabbitmq;

import edu.rutmiit.demo.events.ScooterCreatedEvent;
import edu.rutmiit.demo.events.UserRatedEvent;
import grpc.demo.handler.ScooterNotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);
    private final ScooterNotificationHandler notificationHandler;

    public NotificationEventListener(ScooterNotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    // Слушаем события рейтинга пользователей
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.user.ratings", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void handleUserRatedEvent(UserRatedEvent event) {
        log.info("Получено событие рейтинга пользователя: userId={}, score={}, verdict={}",
                event.userId(), event.ratingScore(), event.verdict());

        // Формируем JSON в формате, который ожидает HTML
        String jsonMessage = String.format(
                "{\"type\":\"USER_RATING_UPDATED\",\"userId\":%d,\"ratingScore\":%d,\"verdict\":\"%s\"}",
                event.userId(),
                event.ratingScore(),
                event.verdict()
        );

        // Отправляем в WebSocket
        notificationHandler.broadcast(jsonMessage);

        log.debug("Отправлено WebSocket уведомление: {}", jsonMessage);
    }

    // Слушаем события создания самокатов
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.scooter.created", durable = "true"),
                    exchange = @Exchange(name = "scooter-rental-exchange", type = "topic"),
                    key = "scooter.created"
            )
    )
    public void handleScooterCreatedEvent(ScooterCreatedEvent event) {
        log.info("Получено событие создания самоката: scooterId={}, model={}, serial={}",
                event.scooterId(), event.model(), event.serialNumber());

        // Формируем JSON для уведомления о создании самоката
        String jsonMessage = String.format(
                "{\"type\":\"SCOOTER_CREATED\",\"scooterId\":%d,\"model\":\"%s\",\"serialNumber\":\"%s\"}",
                event.scooterId(),
                event.model(),
                event.serialNumber()
        );

        // Отправляем в WebSocket
        notificationHandler.broadcast(jsonMessage);

        log.debug("Отправлено WebSocket уведомление: {}", jsonMessage);
    }

    // ДОБАВЬТЕ этот метод для событий удаления самокатов
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notifications.scooter.deleted", durable = "true"),
                    exchange = @Exchange(name = "scooter-rental-exchange", type = "topic"),
                    key = "scooter.deleted"
            )
    )
    public void handleScooterDeletedEvent(edu.rutmiit.demo.events.ScooterDeletedEvent event) {
        log.info("Получено событие удаления самоката: scooterId={}", event.scooterId());

        // Формируем JSON для уведомления об удалении самоката
        String jsonMessage = String.format(
                "{\"type\":\"SCOOTER_DELETED\",\"scooterId\":%d}",
                event.scooterId()
        );

        // Отправляем в WebSocket
        notificationHandler.broadcast(jsonMessage);

        log.debug("Отправлено WebSocket уведомление: {}", jsonMessage);
    }
}