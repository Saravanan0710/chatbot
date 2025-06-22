package com.akash.chatbot.service;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WhatsAppServiceTest {
    private WhatsAppService whatsAppService;
    private Firestore firestore;

    @BeforeEach
    void setUp() {
        firestore = Mockito.mock(Firestore.class);
        whatsAppService = new WhatsAppService(firestore);
    }

    @Test
    void testHandleIncomingMessage() {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setFrom("12345");
        request.setTo("67890");
        request.setMessage("Hello!");
        whatsAppService.handleIncomingMessage(request);
        // No assertion: just ensure no exceptions and logs are printed
    }
} 