package com.akash.chatbot.controller;

import com.akash.chatbot.model.Note;
import com.akash.chatbot.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody Note note) {
        try {
            Note created = noteService.createNote(note.getTitle(), note.getContent());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@PathVariable String id) {
        try {
            Note note = noteService.getNote(id);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable String id, @RequestBody Note note) {
        try {
            Note updated = noteService.updateNote(id, note.getContent());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {
        try {
            boolean deleted = noteService.deleteNote(id);
            if (deleted) return ResponseEntity.ok("Note deleted");
            else return ResponseEntity.badRequest().body("Note not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listNotes() {
        try {
            List<Note> notes = noteService.listNotes();
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 