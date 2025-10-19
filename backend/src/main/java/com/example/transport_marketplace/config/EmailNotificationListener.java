package com.example.transport_marketplace.config;

import com.example.transport_marketplace.events.PaymentSuccessEvent;
import com.example.transport_marketplace.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationListener {
    @Autowired
    private final EmailService emailService;

    @RabbitListener(queues = "payment.email.queue")
    public void handlePaymentNotification(PaymentSuccessEvent event){
        log.info("Получено событие для отправки email: {}", event.getPaymentId());

        try{
            emailService.sendBookingConfirmation(event.getUserEmail(), event);
            log.info("Email успешно отправлен в MailHog для платежа {}", event.getPaymentId());
        } catch (Exception e) {
            log.error("Ошибка отправки email через MailHog для платежа {}", event.getPaymentId(), e);
        }
    }
}