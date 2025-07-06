package com.example.notification_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalService {

    private final RestTemplate restTemplate;

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callEmailProvider(String providerName) {
        try {
            // Симуляция вызова внешнего email провайдера
            String url = "http://" + providerName + "/api/status";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            // Fallback метод
            return "Email provider " + providerName + " is currently unavailable. Using fallback provider.";
        }
    }

    public String callUnreliableEmailService() {
        try {
            // Симуляция ненадежного email сервиса
            double random = Math.random();
            if (random < 0.6) {
                throw new RuntimeException("Email service temporarily unavailable");
            }
            return "Email sent successfully: " + random;
        } catch (Exception e) {
            return "Fallback email response due to: " + e.getMessage();
        }
    }
} 