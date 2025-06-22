package com.akash.chatbot.service;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhatsAppService {
    private final Firestore firestore;
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);

    public void handleIncomingMessage(WhatsAppMessageRequest request) {
        // Save to Firestore (mocked)
        saveMessageToFirestore(request);
        // Reply with static message
        sendMessage(request.getFrom(), "Thank you for your message!");
    }

    public void sendMessage(String to, String message) {
        // Mock sending message to WhatsApp API
        logger.info("Sending message to {}: {}", to, message);
    }

    private void saveMessageToFirestore(WhatsAppMessageRequest request) {
        // Mock Firestore save
        logger.info("Saving message to Firestore: {}", request);
    }
} 