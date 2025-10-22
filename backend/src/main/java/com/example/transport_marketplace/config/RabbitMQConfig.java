package com.example.transport_marketplace.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String SUCCESS_QUEUE = "payment.success.queue";
    public static final String PAYMENT_ROUTING_KEY = "payment.success";

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.success.queue";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.success";

    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue emailQueue(){
        return new Queue(SUCCESS_QUEUE);
    }

    @Bean
    public Binding emailBinding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(PAYMENT_ROUTING_KEY);
    }
}
