package com.akash.chatbot.controller;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import com.akash.chatbot.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final WhatsAppService whatsAppService;
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        try {
            WhatsAppMessageRequest request = parsePayload(payload);
            if (request != null) {
                logger.info("Received message: {}", request);
                whatsAppService.handleIncomingMessage(request);
            } else {
                logger.warn("Could not parse incoming webhook payload: {}", payload);
            }
        } catch (Exception e) {
            logger.error("Error handling webhook payload", e);
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    private WhatsAppMessageRequest parsePayload(Map<String, Object> payload) {
        // Flat format
        if (payload.containsKey("message") && payload.containsKey("from")) {
            WhatsAppMessageRequest req = new WhatsAppMessageRequest();
            req.setFrom((String) payload.get("from"));
            req.setTo((String) payload.getOrDefault("to", ""));
            req.setMessage((String) payload.get("message"));
            return req;
        }
        // WhatsApp Cloud API nested format
        try {
            var entry = ((java.util.List<?>) payload.get("entry")).get(0);
            var changes = ((Map<?, ?>) entry).get("changes");
            var change = ((java.util.List<?>) changes).get(0);
            var value = ((Map<?, ?>) change).get("value");
            var messages = ((Map<?, ?>) value).get("messages");
            var msgObj = ((java.util.List<?>) messages).get(0);
            var from = (String) ((Map<?, ?>) msgObj).get("from");
            var text = ((Map<?, ?>) msgObj).get("text");
            var body = (String) ((Map<?, ?>) text).get("body");
            WhatsAppMessageRequest req = new WhatsAppMessageRequest();
            req.setFrom(from);
            req.setTo("");
            req.setMessage(body);
            return req;
        } catch (Exception e) {
            logger.warn("Failed to parse WhatsApp Cloud API payload", e);
            return null;
        }
    }

    @PostMapping("/api/test/send")
    public ResponseEntity<String> testSend(@RequestBody WhatsAppMessageRequest request) {
        logger.info("Test send triggered: {}", request);
        whatsAppService.sendMessage(request.getTo(), "This is a test message from internal API.");
        return ResponseEntity.ok("Test message sent");
    }
} 