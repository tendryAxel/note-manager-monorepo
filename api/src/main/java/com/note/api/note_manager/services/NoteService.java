package com.note.api.note_manager.services;

import com.note.api.note_manager.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteService {
  private final NoteRepository noteRepository;
}
