package com.akash.chatbot.controller;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import com.akash.chatbot.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final WhatsAppService whatsAppService;
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody WhatsAppMessageRequest request) {
        logger.info("Received message: {}", request);
        whatsAppService.handleIncomingMessage(request);
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    @PostMapping("/api/test/send")
    public ResponseEntity<String> testSend(@RequestBody WhatsAppMessageRequest request) {
        logger.info("Test send triggered: {}", request);
        whatsAppService.sendMessage(request.getTo(), "This is a test message from internal API.");
        return ResponseEntity.ok("Test message sent");
    }
} 