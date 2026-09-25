package com.note.api.note_manager.repository;

import com.note.api.note_manager.models.SessionToken;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {
  List<SessionToken> getByUser_IdAndExpireAtIsAfter(String userId, Date expireAtAfter);

  Optional<SessionToken> findByTokenAndExpireAtAfter(String token, Date expireAtAfter);
}
