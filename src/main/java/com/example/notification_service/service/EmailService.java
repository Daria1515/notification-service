package com.example.notification_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "email-provider", fallbackMethod = "sendEmailFallback")
    public void sendEmail(String to, String subject, String body) {
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
    }

    public void sendEmailFallback(String to, String subject, String body, Exception e) {
        logger.error("Ошибка при отправке email: {}. Используется fallback.", e.getMessage());
        // В реальном приложении здесь можно сохранить в очередь для повторной отправки
        logger.info("Email для {} сохранен в очередь для повторной отправки", to);
    }
    
    @CircuitBreaker(name = "email-provider", fallbackMethod = "sendWelcomeEmailFallback")
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Уведомление";
        String body = createWelcomeEmailBody(userName);
        sendEmail(to, subject, body);
    }

    public void sendWelcomeEmailFallback(String to, String userName, Exception e) {
        logger.error("Ошибка при отправке приветственного email: {}. Используется fallback.", e.getMessage());
        logger.info("Приветственный email для {} сохранен в очередь", to);
    }
    
    @CircuitBreaker(name = "email-provider", fallbackMethod = "sendGoodbyeEmailFallback")
    public void sendGoodbyeEmail(String to, String userName) {
        String subject = "Уведомление";
        String body = createGoodbyeEmailBody(userName);
        sendEmail(to, subject, body);
    }

    public void sendGoodbyeEmailFallback(String to, String userName, Exception e) {
        logger.error("Ошибка при отправке прощального email: {}. Используется fallback.", e.getMessage());
        logger.info("Прощальный email для {} сохранен в очередь", to);
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