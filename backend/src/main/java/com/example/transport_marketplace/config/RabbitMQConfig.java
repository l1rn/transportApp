package com.example.transport_marketplace.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "payment.exchange";
    public static final String QUEUE_NAME = "payment.success.queue";
    public static final String ROUTING_KEY = "payment.success";

    @Bean
    public TopicExchange paymentExchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue emailQueue(){
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding emailBinding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

}
