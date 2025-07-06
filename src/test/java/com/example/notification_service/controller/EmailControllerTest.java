package com.example.notification_service.controller;

import com.example.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    void testSendTestEmail_Success() throws Exception {
        // Arrange
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/send")
                .param("to", "test@example.com")
                .param("subject", "Test Subject")
                .param("body", "Test Body"))
                .andExpect(status().isOk())
                .andExpect(content().string("Письмо успешно отправлено на test@example.com"));
    }

    @Test
    void testSendTestEmail_Error() throws Exception {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/send")
                .param("to", "test@example.com")
                .param("subject", "Test Subject")
                .param("body", "Test Body"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка отправки: SMTP error"));
    }

    @Test
    void testSendWelcomeEmail_Success() throws Exception {
        // Arrange
        doNothing().when(emailService).sendWelcomeEmail(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/welcome")
                .param("to", "test@example.com")
                .param("userName", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Приветственное письмо отправлено на test@example.com"));
    }

    @Test
    void testSendWelcomeEmail_Error() throws Exception {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(emailService).sendWelcomeEmail(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/welcome")
                .param("to", "test@example.com")
                .param("userName", "TestUser"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка отправки: SMTP error"));
    }

    @Test
    void testSendGoodbyeEmail_Success() throws Exception {
        // Arrange
        doNothing().when(emailService).sendGoodbyeEmail(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/goodbye")
                .param("to", "test@example.com")
                .param("userName", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Прощальное письмо отправлено на test@example.com"));
    }

    @Test
    void testSendGoodbyeEmail_Error() throws Exception {
        // Arrange
        doThrow(new RuntimeException("SMTP error")).when(emailService).sendGoodbyeEmail(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/email/goodbye")
                .param("to", "test@example.com")
                .param("userName", "TestUser"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка отправки: SMTP error"));
    }

    @Test
    void testTestEmailService() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/email/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email сервис работает корректно"));
    }
}
