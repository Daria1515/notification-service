package com.example.notification_service.controller;
//API
import com.example.notification_service.dto.EmailRequest;
import com.example.notification_service.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody EmailRequest request) {
        service.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.ok("Письмо отправлено");
    }
}