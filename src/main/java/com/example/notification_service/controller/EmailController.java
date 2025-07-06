package com.example.notification_service.controller;

import com.example.notification_service.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@Tag(name = "Email Service", description = "API для тестирования отправки email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @Operation(summary = "Отправить тестовое письмо", description = "Отправляет тестовое письмо на указанный email")
    public ResponseEntity<String> sendTestEmail(
            @Parameter(description = "Email получателя", required = true) 
            @RequestParam String to,
            @Parameter(description = "Тема письма", required = true) 
            @RequestParam String subject,
            @Parameter(description = "Текст письма", required = true) 
            @RequestParam String body) {
        
        try {
            emailService.sendEmail(to, subject, body);
            return ResponseEntity.ok("Письмо успешно отправлено на " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка отправки: " + e.getMessage());
        }
    }

    @PostMapping("/welcome")
    @Operation(summary = "Отправить приветственное письмо", description = "Отправляет красивое приветственное письмо")
    public ResponseEntity<String> sendWelcomeEmail(
            @Parameter(description = "Email получателя", required = true) 
            @RequestParam String to,
            @Parameter(description = "Имя пользователя", required = true) 
            @RequestParam String userName) {
        
        try {
            emailService.sendWelcomeEmail(to, userName);
            return ResponseEntity.ok("Приветственное письмо отправлено на " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка отправки: " + e.getMessage());
        }
    }

    @PostMapping("/goodbye")
    @Operation(summary = "Отправить прощальное письмо", description = "Отправляет прощальное письмо")
    public ResponseEntity<String> sendGoodbyeEmail(
            @Parameter(description = "Email получателя", required = true) 
            @RequestParam String to,
            @Parameter(description = "Имя пользователя", required = true) 
            @RequestParam String userName) {
        
        try {
            emailService.sendGoodbyeEmail(to, userName);
            return ResponseEntity.ok("Прощальное письмо отправлено на " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка отправки: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Тест email сервиса", description = "Проверяет доступность email сервиса")
    public ResponseEntity<String> testEmailService() {
        return ResponseEntity.ok("Email сервис работает корректно");
    }
}