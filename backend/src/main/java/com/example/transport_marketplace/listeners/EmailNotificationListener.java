package com.example.transport_marketplace.listeners;

import com.example.transport_marketplace.events.ConfirmationCodeEvent;
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

    @RabbitListener(queues = "confirmation.code.queue")
    public void handlePaymentNotification(ConfirmationCodeEvent event){
        log.info("Получено событие для отправки email: {}", event.getPaymentId());

        try{
            emailService.sendPaymentCode(event.getUserEmail(), event);
            log.info("Email успешно отправлен в MailHog для платежа {}", event.getPaymentId());
        } catch (Exception e) {
            log.error("Ошибка отправки email через MailHog для платежа {}", event.getPaymentId(), e);
        }
    }

    @RabbitListener(queues = "payment.success.queue")
    public void handlePaymentSuccessEvent(PaymentSuccessEvent event){
        try {
            emailService.sendPaymentConfirmation(event.getUserEmail(), event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}