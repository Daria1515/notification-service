package com.example.notification_service.kafka;

import com.example.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final EmailService emailService;

    public KafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(UserEvent event) {
        String message = switch (event.getOperation()) {
            case "CREATE" -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
            case "DELETE" -> "Здравствуйте! Ваш аккаунт был удалён.";
            default -> "Операция неизвестна";
        };
        emailService.sendEmail(event.getEmail(), "Уведомление", message);
    }
}
