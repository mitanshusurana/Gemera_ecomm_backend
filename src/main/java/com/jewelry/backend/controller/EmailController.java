package com.jewelry.backend.controller;

import com.jewelry.backend.entity.EmailNotification;
import com.jewelry.backend.entity.EmailTemplate;
import com.jewelry.backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/email")
@CrossOrigin(origins = "*")
@Tag(name = "Email", description = "Email Notification APIs")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @Operation(summary = "Send email notification")
    public ResponseEntity<EmailNotification> sendEmail(@RequestBody EmailNotification notification) {
        return ResponseEntity.ok(emailService.sendEmail(notification));
    }

    @GetMapping("/notifications/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<EmailNotification> getNotification(@PathVariable UUID id) {
        return ResponseEntity.ok(emailService.getNotification(id));
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<Page<EmailNotification>> getNotifications(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(emailService.getUserNotifications(email, PageRequest.of(page, size)));
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe to newsletter")
    public ResponseEntity<Void> subscribe(@RequestBody Map<String, String> body) {
        emailService.subscribe(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    @Operation(summary = "Unsubscribe from newsletter")
    public ResponseEntity<Void> unsubscribe(@RequestBody Map<String, String> body) {
        emailService.unsubscribe(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/templates/{name}")
    @Operation(summary = "Get email template")
    public ResponseEntity<EmailTemplate> getTemplate(@PathVariable String name) {
        return ResponseEntity.ok(emailService.getTemplate(name));
    }

    @GetMapping("/templates")
    @Operation(summary = "Get all templates")
    public ResponseEntity<List<EmailTemplate>> getAllTemplates() {
        return ResponseEntity.ok(emailService.getAllTemplates());
    }
}
