package com.akhlaq.securenote.repositories;

import com.akhlaq.securenote.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepo extends JpaRepository<Note, Long> {
    Optional<Note> findById(Long id);
    Optional<List<Note>> findByOwnerUsername(String username);
}