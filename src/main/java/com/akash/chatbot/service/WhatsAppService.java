package com.akash.chatbot.service;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.akash.chatbot.model.Note;

@Service
@RequiredArgsConstructor
public class WhatsAppService {
    private final NoteService noteService;
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);

    public void handleIncomingMessage(WhatsAppMessageRequest request) {
        String message = request.getMessage().trim();
        String from = request.getFrom();
        try {
            String lower = message.toLowerCase();
            if (lower.equals("hello") || lower.equals("hi") || lower.equals("help")) {
                sendMessage(from, getHelpText());
            } else if (lower.startsWith("create note")) {
                String title = message.substring("create note".length()).trim();
                Note note = noteService.createNote(title, "");
                sendMessage(from, "Note created with ID: " + note.getId());
            } else if (lower.startsWith("get note")) {
                String id = message.substring("get note".length()).trim();
                Note note = noteService.getNote(id);
                sendMessage(from, note != null ? "Note: " + note.getTitle() + " - " + note.getContent() : "Note not found");
            } else if (lower.startsWith("update note")) {
                String[] parts = message.substring("update note".length()).trim().split(" ", 2);
                if (parts.length < 2) {
                    sendMessage(from, "Usage: update note <id> <content>");
                    return;
                }
                String id = parts[0];
                String content = parts[1];
                Note updated = noteService.updateNote(id, content);
                sendMessage(from, "Note updated: " + updated.getTitle() + " - " + updated.getContent());
            } else if (lower.startsWith("delete note")) {
                String id = message.substring("delete note".length()).trim();
                boolean deleted = noteService.deleteNote(id);
                sendMessage(from, deleted ? "Note deleted." : "Note not found.");
            } else if (lower.equals("list notes")) {
                var notes = noteService.listNotes();
                if (notes.isEmpty()) {
                    sendMessage(from, "No notes found.");
                } else {
                    StringBuilder sb = new StringBuilder("Notes:\n");
                    for (Note n : notes) {
                        sb.append(n.getId()).append(": ").append(n.getTitle()).append(" - ").append(n.getContent()).append("\n");
                    }
                    sendMessage(from, sb.toString());
                }
            } else {
                sendMessage(from, getFallbackText());
            }
        } catch (Exception e) {
            logger.error("Error handling message", e);
            sendMessage(from, "An error occurred: " + e.getMessage());
        }
    }

    private String getHelpText() {
        return "Hi! I can help you manage notes.\n" +
                "Commands:\n" +
                "- create note <title>\n" +
                "- get note <id>\n" +
                "- update note <id> <content>\n" +
                "- delete note <id>\n" +
                "- list notes";
    }

    private String getFallbackText() {
        return "Sorry, I didn't understand that. Try 'create note <title>' or 'list notes'.";
    }

    public void sendMessage(String to, String message) {
        // Mock sending message to WhatsApp API
        logger.info("Sending message to {}: {}", to, message);
    }
} 