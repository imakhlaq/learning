package com.akhlaq.securenote.controllers;

import com.akhlaq.securenote.models.Note;
import com.akhlaq.securenote.repositories.NoteRepo;
import com.akhlaq.securenote.services.INoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/notes")
public class NoteController {
    private final INoteService noteService;

    @PostMapping
    public Note CreateNote(@RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {
        var username = userDetails.getUsername();
        log.debug("Creating note for user: {}", username);
        return noteService.createNoteForUser(username, content);
    }

    @PostMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        var username = userDetails.getUsername();
        log.debug("Getting notes for user: {}", username);
        return noteService.findNoteForUser(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId, @RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        var username = userDetails.getUsername();
        log.debug("Updating note for user: {}", username);
        return noteService.updateNoteForUser(noteId, username, content);
    }

}