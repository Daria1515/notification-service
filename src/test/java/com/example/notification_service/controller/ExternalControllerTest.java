package com.example.notification_service.controller;

import com.example.notification_service.service.ExternalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExternalController.class)
class ExternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalService externalService;

    @Test
    void testCallEmailProvider_Success() throws Exception {
        // Arrange
        when(externalService.callEmailProvider(anyString()))
                .thenReturn("Email provider gmail-provider is healthy");

        // Act & Assert
        mockMvc.perform(get("/api/external/email-provider/gmail-provider"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email provider gmail-provider is healthy"));
    }

    @Test
    void testCallEmailProvider_Fallback() throws Exception {
        // Arrange
        when(externalService.callEmailProvider(anyString()))
                .thenReturn("Email provider gmail-provider is currently unavailable. Using fallback provider.");

        // Act & Assert
        mockMvc.perform(get("/api/external/email-provider/gmail-provider"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("fallback")));
    }

    @Test
    void testCallUnreliableEmailService_Success() throws Exception {
        // Arrange
        when(externalService.callUnreliableEmailService())
                .thenReturn("Email sent successfully: 0.8");

        // Act & Assert
        mockMvc.perform(get("/api/external/unreliable-email"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully: 0.8"));
    }

    @Test
    void testCallUnreliableEmailService_Fallback() throws Exception {
        // Arrange
        when(externalService.callUnreliableEmailService())
                .thenReturn("Fallback email response due to: Email service temporarily unavailable");

        // Act & Assert
        mockMvc.perform(get("/api/external/unreliable-email"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Fallback email response")));
    }

    @Test
    void testHealth() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/external/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification Service is healthy"));
    }
} 