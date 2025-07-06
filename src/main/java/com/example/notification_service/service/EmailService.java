package com.example.notification_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender sender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            logger.info("=== EMAIL ОТПРАВКА ===");
            logger.info("От: {}", fromEmail);
            logger.info("Кому: {}", to);
            logger.info("Тема: {}", subject);
            logger.info("Текст: {}", body);
            logger.info("======================");
            
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true для HTML
            
            sender.send(message);
            logger.info("Email успешно отправлен на {}", to);
            
        } catch (MessagingException e) {
            logger.error("Ошибка при отправке email: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при отправке письма: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отправке email: {}", e.getMessage(), e);
            throw new RuntimeException("Неожиданная ошибка при отправке письма: " + e.getMessage(), e);
        }
    }
    
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Уведомление";
        String body = createWelcomeEmailBody(userName);
        sendEmail(to, subject, body);
    }
    
    public void sendGoodbyeEmail(String to, String userName) {
        String subject = "Уведомление";
        String body = createGoodbyeEmailBody(userName);
        sendEmail(to, subject, body);
    }
    
    private String createWelcomeEmailBody(String userName) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 10px;">
                    <p>Здравствуйте! Ваш аккаунт успешно создан.</p>
                </div>
            </body>
            </html>
            """;
    }
    
    private String createGoodbyeEmailBody(String userName) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background-color: #f8f9fa; padding: 20px; border-radius: 10px;">
                    <p>Здравствуйте! Ваш аккаунт был удален.</p>
                </div>
            </body>
            </html>
            """;
    }
}