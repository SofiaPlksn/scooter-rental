package edu.rutmiit.demo.demorest.listeners;

import edu.rutmiit.demo.events.UserRatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InternalAnalyticsListener {

    private static final Logger log = LoggerFactory.getLogger(InternalAnalyticsListener.class);

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.demorest.analytics.log", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void handleUserRatedEvent(UserRatedEvent event) {
        log.info("INTERNAL LOG: User {} rated with score {} - verdict: {}",
                event.userId(), event.ratingScore(), event.verdict());
    }
}