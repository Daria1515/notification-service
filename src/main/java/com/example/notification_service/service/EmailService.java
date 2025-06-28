package com.example.notification_service.service;
//логика отправки писем
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender sender;

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            logger.info("=== EMAIL ОТПРАВКА ===");
            logger.info("Кому: {}", to);
            logger.info("Тема: {}", subject);
            logger.info("Текст: {}", body);
            logger.info("======================");
            
            logger.info("Email успешно обработан");
        } catch (Exception e) {
            logger.error("Ошибка при обработке email: {}", e.getMessage());
            throw new RuntimeException("Ошибка при отправке письма: " + e.getMessage(), e);
        }
    }
}