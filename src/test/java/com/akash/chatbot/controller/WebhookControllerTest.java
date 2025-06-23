package com.akash.chatbot.controller;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import com.akash.chatbot.service.WhatsAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WebhookControllerTest {
    private WhatsAppService whatsAppService;
    private WebhookController webhookController;

    @BeforeEach
    void setUp() {
        whatsAppService = Mockito.mock(WhatsAppService.class);
        webhookController = new WebhookController(whatsAppService);
    }

    @Test
    void testReceiveWebhook_flatPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", "12345");
        payload.put("to", "67890");
        payload.put("message", "create note Buy milk");
        ResponseEntity<String> response = webhookController.receiveWebhook(payload);
        assertEquals("EVENT_RECEIVED", response.getBody());
        verify(whatsAppService).handleIncomingMessage(any(WhatsAppMessageRequest.class));
    }

    @Test
    void testReceiveWebhook_whatsappCloudPayload() {
        Map<String, Object> text = new HashMap<>();
        text.put("body", "create note Buy milk");
        Map<String, Object> msg = new HashMap<>();
        msg.put("from", "12345");
        msg.put("text", text);
        Map<String, Object> value = new HashMap<>();
        value.put("messages", List.of(msg));
        Map<String, Object> change = new HashMap<>();
        change.put("value", value);
        Map<String, Object> entry = new HashMap<>();
        entry.put("changes", List.of(change));
        Map<String, Object> payload = new HashMap<>();
        payload.put("entry", List.of(entry));
        ResponseEntity<String> response = webhookController.receiveWebhook(payload);
        assertEquals("EVENT_RECEIVED", response.getBody());
        verify(whatsAppService).handleIncomingMessage(any(WhatsAppMessageRequest.class));
    }
} 