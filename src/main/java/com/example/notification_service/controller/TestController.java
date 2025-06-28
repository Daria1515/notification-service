package com.example.notification_service.controller;

import com.example.notification_service.kafka.UserEvent;
import com.example.notification_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final EmailService emailService;

    public TestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public String sendTestEmail(@RequestBody UserEvent event) {
        logger.info("Получено событие (эмуляция Kafka): {}", event);
        
        try {
            // Эмулируем работу Kafka listener
            String message = switch (event.getOperation()) {
                case UserEvent.OPERATION_CREATE -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
                case UserEvent.OPERATION_DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
                default -> "Операция неизвестна";
            };
            
            logger.info("Отправляем email на {} с сообщением: {}", event.getEmail(), message);
            emailService.sendEmail(event.getEmail(), "Уведомление", message);
            logger.info("Email успешно отправлен");
            
            return "Email отправлен успешно на " + event.getEmail() + " с сообщением: " + message;
        } catch (Exception e) {
            logger.error("Ошибка отправки email: {}", e.getMessage(), e);
            return "Ошибка отправки email: " + e.getMessage();
        }
    }
} 