package com.note.api.note_manager.services;

import com.note.api.note_manager.models.Note;
import com.note.api.note_manager.repository.NoteRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteService {
  private final NoteRepository noteRepository;

  public Note save(Note toSave) {
    return noteRepository.save(toSave);
  }

  public List<Note> getNotesByUserId(String userId) {
    return noteRepository.getByUser_Id(userId);
  }

  public Note getById(String noteId) {
    return noteRepository
        .findById(noteId)
        .orElseThrow(() -> new RuntimeException("Note with id %s Not Found".formatted(noteId)));
  }

  public Note deleteById(String noteId) {
    var note = getById(noteId);
    noteRepository.deleteById(noteId);
    return note;
  }

  public boolean doUserOwnThisNote(String userId, String noteId) {
    return noteRepository.existsByUser_IdAndId(userId, noteId);
  }
}
