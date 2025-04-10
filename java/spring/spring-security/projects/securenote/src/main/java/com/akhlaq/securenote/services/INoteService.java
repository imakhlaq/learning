package com.akhlaq.securenote.services;

import com.akhlaq.securenote.models.Note;

import java.util.List;
import java.util.Optional;

public interface INoteService {
    Note createNoteForUser(String username, String content);
    Note updateNoteForUser(Long noteId, String content, String username);
    void deleteNoteForUser(Long noteId, String username);
    List<Note> findNoteForUser(String username);
}