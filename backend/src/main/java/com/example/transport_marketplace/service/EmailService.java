package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.events.ConfirmationCodeEvent;
import com.example.transport_marketplace.events.PaymentSuccessEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, ConfirmationCodeEvent event){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@transport-marketplace.com");
            helper.setTo(toEmail);
            helper.setSubject("✅ Подтверждение бронирования #" + event.getUserEmail());
            String emailContent = buildEmailContent(event);
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendConfirmationCodeNewEmail(String userEmail, String code){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@ololotravel.com");
            message.setTo(userEmail);
            message.setSubject("Email Confirmation Code");
            message.setText("Your confirmation code is: " + code);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отправить email: " + e.getMessage());
        }
    }

    public void sendConfirmationCodeTopUp(String userEmail, String code, double amount){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@ololotravel.com");
            message.setTo(userEmail);
            message.setSubject("Top up Confirmation Code");
            message.setText("Your confirmation code is: " + code + "\nYour amount is: " + amount);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отправить email: " + e.getMessage());
        }
    }

    public String buildEmailContent(ConfirmationCodeEvent event){
        return """
                <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: Arial, sans-serif; }
                            .header { background: #4CAF50; color: white; padding: 20px; }
                            .content { padding: 20px; }
                            .footer { background: #f5f5f5; padding: 10px; }
                        </style>
                    </head>
                    <body>
                        <div class="header">
                            <h1>Код для подтвержденя!</h1>
                        </div>
                        <div class="content">
                            <p>Здравствуйте, <strong>%s</strong>!</p>
                            <p>Ваше бронирование на этапе оплаты.</p>
                            <h3>Детали бронирования:</h3>
                            <ul>
                                <li><strong>Сумма:</strong> %s руб.</li>
                                <li><strong>Код подтверждения:</strong> %s</li>
                                <li>Время создания: %s</li>
                            </ul>
                            <p>Спасибо, что выбрали наш сервис!</p>
                        </div>
                        <div class="footer">
                            <p>Это тестовое письмо отправлено через MailHog</p>
                        </div>
                    </body>
                    </html>
                """.formatted(
                        event.getUserName(),
                        event.getAmount(),
                        event.getConfirmationCode(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        );
    }
}
