package com.akash.chatbot.dto;

import lombok.Data;

@Data
public class WhatsAppMessageRequest {
    private String from;
    private String to;
    private String message;
    @Override
    public String toString() {
        return "WhatsAppMessageRequest{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
} 