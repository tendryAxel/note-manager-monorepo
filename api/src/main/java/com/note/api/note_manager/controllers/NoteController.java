package com.note.api.note_manager.controllers;

import com.note.api.note_manager.services.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NoteController {
  private final NoteService noteService;
}
