package com.note.api.note_manager.repository;

import com.note.api.note_manager.models.Note;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
  List<Note> getByUser_Id(String userId);

  Optional<Note> findById(String id);

  boolean existsByUser_IdAndId(String userId, String id);
}
