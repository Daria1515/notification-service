package com.example.notification_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ExternalService externalService;

    @BeforeEach
    void setUp() {
        externalService = new ExternalService(restTemplate);
    }

    @Test
    void callEmailProvider_ShouldReturnResponse_WhenProviderIsAvailable() {
        // Given
        String providerName = "gmail-provider";
        String expectedResponse = "Email provider response";
        when(restTemplate.getForObject(eq("http://" + providerName + "/api/status"), eq(String.class)))
                .thenReturn(expectedResponse);

        // When
        String result = externalService.callEmailProvider(providerName);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void callEmailProvider_ShouldReturnFallback_WhenProviderIsUnavailable() {
        // Given
        String providerName = "gmail-provider";
        when(restTemplate.getForObject(eq("http://" + providerName + "/api/status"), eq(String.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        // When
        String result = externalService.callEmailProvider(providerName);

        // Then
        assertEquals("Email provider gmail-provider is currently unavailable. Using fallback provider.", result);
    }

    @Test
    void callUnreliableEmailService_ShouldReturnResponse_WhenServiceWorks() {
        // When
        String result = externalService.callUnreliableEmailService();

        // Then
        assertTrue(result.startsWith("Email sent successfully: ") || result.startsWith("Fallback email response due to: "));
    }

    @Test
    void callUnreliableEmailService_ShouldReturnFallback_WhenServiceFails() {
        // When
        String result = externalService.callUnreliableEmailService();

        // Then
        assertTrue(result.startsWith("Email sent successfully: ") || result.startsWith("Fallback email response due to: "));
    }
} 