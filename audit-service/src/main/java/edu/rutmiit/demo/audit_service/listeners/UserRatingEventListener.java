package edu.rutmiit.demo.audit_service.listeners;

import edu.rutmiit.demo.events.UserRatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserRatingEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserRatingEventListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    // Уникальная очередь для audit-service
                    value = @Queue(name = "q.audit.user.ratings", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void handleUserRatedEvent(UserRatedEvent event) {
        log.info("AUDIT: User rating calculated - UserID: {}, Score: {}, Verdict: {}",
                event.userId(), event.ratingScore(), event.verdict());
    }
}


