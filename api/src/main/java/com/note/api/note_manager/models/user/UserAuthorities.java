package com.note.api.note_manager.models.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthorities implements GrantedAuthority {
  USER,
  ADMIN,
  ;

  @Override
  public String getAuthority() {
    return name();
  }
}
