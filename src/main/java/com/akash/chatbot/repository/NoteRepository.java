package com.akash.chatbot.repository;

import com.akash.chatbot.model.Note;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class NoteRepository {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "notes";
    private static final Logger logger = LoggerFactory.getLogger(NoteRepository.class);

    public Note save(Note note) throws ExecutionException, InterruptedException {
        DocumentReference docRef;
        if (note.getId() == null || note.getId().isEmpty()) {
            docRef = firestore.collection(COLLECTION_NAME).document();
            note.setId(docRef.getId());
        } else {
            docRef = firestore.collection(COLLECTION_NAME).document(note.getId());
        }
        docRef.set(note).get();
        return note;
    }

    public Note findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection(COLLECTION_NAME).document(id).get().get();
        return snapshot.exists() ? snapshot.toObject(Note.class) : null;
    }

    public List<Note> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Note> notes = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            notes.add(doc.toObject(Note.class));
        }
        return notes;
    }

    public boolean deleteById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        if (docRef.get().get().exists()) {
            docRef.delete().get();
            return true;
        }
        return false;
    }
} 