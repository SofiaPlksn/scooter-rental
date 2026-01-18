package edu.rutmiit.demo.demorest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "scooter-rental-exchange";
    public static final String ROUTING_KEY_SCOOTER_CREATED = "scooter.created";
    public static final String ROUTING_KEY_SCOOTER_DELETED = "scooter.deleted";

    // Fanout для аналитики
    public static final String FANOUT_EXCHANGE_ANALYTICS = "analytics-fanout";

    @Bean
    public TopicExchange scooterRentalExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public FanoutExchange analyticsExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_ANALYTICS, true, false);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}