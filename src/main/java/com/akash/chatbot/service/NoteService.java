package com.akash.chatbot.service;

import com.akash.chatbot.model.Note;
import com.akash.chatbot.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    public Note createNote(String title, String content) throws Exception {
        try {
            Note note = new Note(null, title, content);
            return noteRepository.save(note);
        } catch (Exception e) {
            logger.error("Failed to create note", e);
            throw new Exception("Failed to create note");
        }
    }

    public Note getNote(String id) throws Exception {
        try {
            Note note = noteRepository.findById(id);
            if (note == null) throw new Exception("Note not found");
            return note;
        } catch (Exception e) {
            logger.error("Failed to get note", e);
            throw new Exception("Failed to get note");
        }
    }

    public Note updateNote(String id, String content) throws Exception {
        try {
            Note note = noteRepository.findById(id);
            if (note == null) throw new Exception("Note not found");
            note.setContent(content);
            return noteRepository.save(note);
        } catch (Exception e) {
            logger.error("Failed to update note", e);
            throw new Exception("Failed to update note");
        }
    }

    public boolean deleteNote(String id) throws Exception {
        try {
            return noteRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Failed to delete note", e);
            throw new Exception("Failed to delete note");
        }
    }

    public List<Note> listNotes() throws Exception {
        try {
            return noteRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to list notes", e);
            throw new Exception("Failed to list notes");
        }
    }
} 