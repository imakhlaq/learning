package com.akhlaq.securenote.services.impl;

import com.akhlaq.securenote.models.Note;
import com.akhlaq.securenote.repositories.NoteRepo;
import com.akhlaq.securenote.services.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImpl implements INoteService {
    private final NoteRepo noteRepo;
    @Override
    public Note createNoteForUser(String username, String content) {
        var note = new Note();
        note.setContent(content);
        note.setOwner(username);
        return noteRepo.save(note);
    }
    @Override
    public Note updateNoteForUser(Long noteId, String content, String username) {
        var note = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        return noteRepo.save(note);
    }
    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        var note = noteRepo.findById(noteId).orElseThrow(() -> new RuntimeException("Note not found"));
        if (note.getOwner().equals(username)) {
            noteRepo.delete(note);
        } else throw new SecurityException("You do not have permission to delete this note");
    }
    @Override
    public List<Note> findNoteForUser(String username) {
        return noteRepo.findByOwnerUsername(username).orElseThrow(() -> new RuntimeException("Note not found"));
    }
}