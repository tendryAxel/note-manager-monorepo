package com.note.api.note_manager.repository;

import com.note.api.note_manager.models.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {}
