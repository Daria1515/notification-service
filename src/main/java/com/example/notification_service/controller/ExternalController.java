package com.example.notification_service.controller;

import com.example.notification_service.service.ExternalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
@Tag(name = "External Services", description = "API для тестирования Circuit Breaker")
public class ExternalController {

    @Autowired
    private ExternalService externalService;

    @GetMapping("/email-provider/{providerName}")
    @Operation(summary = "Вызвать email провайдера", description = "Демонстрирует работу Circuit Breaker с email провайдерами")
    public ResponseEntity<String> callEmailProvider(
            @Parameter(description = "Имя email провайдера", required = true) 
            @PathVariable String providerName) {
        String result = externalService.callEmailProvider(providerName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/unreliable-email")
    @Operation(summary = "Вызвать ненадежный email сервис", description = "Демонстрирует Circuit Breaker с fallback")
    public ResponseEntity<String> callUnreliableEmailService() {
        String result = externalService.callUnreliableEmailService();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Проверка здоровья сервиса", description = "Возвращает статус сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is healthy");
    }
} 