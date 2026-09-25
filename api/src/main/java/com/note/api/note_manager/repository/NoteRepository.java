package com.note.api.note_manager.repository;

import com.note.api.note_manager.models.Note;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
  List<Note> getByUser_Id(String userId);
}
