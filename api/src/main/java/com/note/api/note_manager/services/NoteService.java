package com.note.api.note_manager.services;

import com.note.api.note_manager.models.Note;
import com.note.api.note_manager.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteService {
  private final NoteRepository noteRepository;

  public Note save(Note toSave) {
    return noteRepository.save(toSave);
  }
}
