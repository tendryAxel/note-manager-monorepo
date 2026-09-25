package com.note.api.note_manager.controllers;

import static com.note.api.note_manager.config.AuthProvider.getPrincipal;

import com.note.api.note_manager.rest.model.CreateNoteRequest;
import com.note.api.note_manager.rest.model.Note;
import com.note.api.note_manager.services.NoteService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class NoteController {
  private final NoteService noteService;

  @GetMapping("/api/v1/notes")
  public List<Note> getNotes() {
    var principal = getPrincipal();
    return principal.getNotes().stream()
        .map(
            note ->
                new Note()
                    .id(note.getId())
                    .title(note.getTitle())
                    .content(note.getContent())
                    .userId(principal.getId())
                    .createdAt(note.getCreatedAt().toInstant()))
        .toList();
  }

  @PostMapping("/api/v1/notes")
  public Note createNote(@RequestBody CreateNoteRequest request) {
    var principal = getPrincipal();
    var saved =
        noteService.save(
            new com.note.api.note_manager.models.Note(
                UUID.randomUUID().toString(),
                principal,
                new Date(),
                request.getTitle(),
                request.getContent()));
    Note note =
        new Note()
            .id(saved.getId())
            .content(saved.getContent())
            .title(saved.getTitle())
            .userId(principal.getId())
            .createdAt(saved.getCreatedAt().toInstant());
    log.info("Note => " + note);
    return note;
  }
}
