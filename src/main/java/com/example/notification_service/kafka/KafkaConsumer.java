package com.example.notification_service.kafka;

import com.example.notification_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final EmailService emailService;

    public KafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(UserEvent event, 
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                      @Header(KafkaHeaders.OFFSET) long offset) {
        logger.info("Получено событие из Kafka: topic={}, partition={}, offset={}, event={}",
                   topic, partition, offset, event);
        
        try {
            // Извлекаем имя пользователя из email (часть до @)
            String userName = extractUserName(event.getEmail());
            
            switch (event.getOperation()) {
                case UserEvent.OPERATION_CREATE -> {
                    logger.info("Отправляем приветственное письмо на {}", event.getEmail());
                    emailService.sendWelcomeEmail(event.getEmail(), userName);
                    logger.info("Приветственное письмо успешно отправлено");
                }
                case UserEvent.OPERATION_DELETE -> {
                    logger.info("Отправляем прощальное письмо на {}", event.getEmail());
                    emailService.sendGoodbyeEmail(event.getEmail(), userName);
                    logger.info("Прощальное письмо успешно отправлено");
                }
                default -> {
                    logger.warn("Неизвестная операция: {}", event.getOperation());
                    String message = "Произошла операция с вашим аккаунтом: " + event.getOperation();
                    emailService.sendEmail(event.getEmail(), "Уведомление о системе", message);
                }
            }
            
        } catch (Exception e) {
            logger.error("Ошибка при обработке события: {}", e.getMessage(), e);            
        }
    }
    
    private String extractUserName(String email) {
        if (email == null || email.isEmpty()) {
            return "Пользователь";
        }
        
        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            String userName = email.substring(0, atIndex);            
            return userName.substring(0, 1).toUpperCase() + userName.substring(1);
        }
        
        return email;
    }
}

