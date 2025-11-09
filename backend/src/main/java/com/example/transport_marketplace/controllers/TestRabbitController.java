package com.example.transport_marketplace.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/rabbitmq")
@RequiredArgsConstructor
public class TestRabbitController {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/send-test")
    public String sendTestMessage(){
        try{
            Map<String, Object> testMessage = Map.of(
                    "message", "Test message from Postman",
                    "timestamp", LocalDateTime.now().toString(),
                    "test", true
            );

            rabbitTemplate.convertAndSend(
                    "payment.exchange",
                    "payment.success",
                    testMessage
            );

            return "✅ Сообщение отправлено в RabbitMQ: " + testMessage;

        } catch (Exception e) {
            return "❌ Ошибка: " + e.getMessage();
        }
    }

    @GetMapping("/health")
    public String checkRabbitMQHealth(){
        try{
            rabbitTemplate.execute(channel -> "✅ RabbitMQ подключен. Channel: " + channel.getChannelNumber());
            return "✅ RabbitMQ работает нормально";
        } catch (Exception e) {
            return "❌ RabbitMQ недоступен: " + e.getMessage();
        }
    }
}
