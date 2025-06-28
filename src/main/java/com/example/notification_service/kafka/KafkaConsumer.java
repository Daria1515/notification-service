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
            String message = switch (event.getOperation()) {
                case UserEvent.OPERATION_CREATE -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
                case UserEvent.OPERATION_DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
                default -> "Операция неизвестна";
            };
            
            logger.info("Отправляем email на {} с сообщением: {}", event.getEmail(), message);
            emailService.sendEmail(event.getEmail(), "Уведомление", message);
            logger.info("Email успешно отправлен");
            
        } catch (Exception e) {
            logger.error("Ошибка при обработке события: {}", e.getMessage(), e);
        }
    }
}
