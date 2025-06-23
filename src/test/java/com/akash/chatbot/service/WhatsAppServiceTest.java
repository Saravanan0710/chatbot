package com.akash.chatbot.service;

import com.akash.chatbot.dto.WhatsAppMessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.akash.chatbot.model.Note;
import static org.mockito.Mockito.*;

class WhatsAppServiceTest {
    private WhatsAppService whatsAppService;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteService = Mockito.mock(NoteService.class);
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

    @Test
    void testHandleIncomingMessage_createNote() throws Exception {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setFrom("12345");
        request.setTo("67890");
        request.setMessage("create note TestTitle");
        Note note = new Note("1", "TestTitle", "");
        when(noteService.createNote("TestTitle", "")).thenReturn(note);
        whatsAppService = spy(whatsAppService);
        doNothing().when(whatsAppService).sendMessage(anyString(), anyString());
        whatsAppService.handleIncomingMessage(request);
        verify(whatsAppService).sendMessage("12345", "Note created with ID: 1");
    }

    @Test
    void testHandleIncomingMessage_listNotes() throws Exception {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setFrom("12345");
        request.setTo("67890");
        request.setMessage("list notes");
        when(noteService.listNotes()).thenReturn(java.util.List.of(new Note("1", "Title1", "Content1")));
        whatsAppService = spy(whatsAppService);
        doNothing().when(whatsAppService).sendMessage(anyString(), anyString());
        whatsAppService.handleIncomingMessage(request);
        verify(whatsAppService).sendMessage(contains("12345"), contains("Notes:"));
    }

    @Test
    void testHandleIncomingMessage_greetingHelp() {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setFrom("12345");
        request.setTo("67890");
        request.setMessage("hello");
        WhatsAppService spyService = spy(whatsAppService);
        doNothing().when(spyService).sendMessage(anyString(), anyString());
        spyService.handleIncomingMessage(request);
        verify(spyService).sendMessage(eq("12345"), contains("I can help you manage notes"));

        request.setMessage("help");
        spyService.handleIncomingMessage(request);
        verify(spyService, atLeastOnce()).sendMessage(eq("12345"), contains("I can help you manage notes"));
    }

    @Test
    void testHandleIncomingMessage_fallback() {
        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setFrom("12345");
        request.setTo("67890");
        request.setMessage("random text");
        WhatsAppService spyService = spy(whatsAppService);
        doNothing().when(spyService).sendMessage(anyString(), anyString());
        spyService.handleIncomingMessage(request);
        verify(spyService).sendMessage(eq("12345"), contains("Sorry, I didn't understand that"));
    }
} 