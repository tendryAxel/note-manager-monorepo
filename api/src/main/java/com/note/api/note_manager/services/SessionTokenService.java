package com.note.api.note_manager.services;

import com.note.api.note_manager.models.SessionToken;
import com.note.api.note_manager.repository.SessionTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionTokenService {
  private final SessionTokenRepository sessionTokenRepository;

  public SessionToken save(SessionToken toSave) {
    return sessionTokenRepository.save(toSave);
  }
}
