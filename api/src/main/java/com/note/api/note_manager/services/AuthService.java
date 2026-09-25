package com.note.api.note_manager.services;

import com.note.api.note_manager.models.SessionToken;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtServices jwtServices;
  private final UserInfoService userInfoService;
  private final SessionTokenService sessionTokenService;

  public String loginWithToken(String email, String password) {
    var user = userInfoService.getByEmail(email);
    var token = jwtServices.generateToken(user);
    sessionTokenService.save(new SessionToken(token, user, Duration.ofDays(1)));
    var bearer = "Bearer %s".formatted(token);
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, bearer));
    return token;
  }
}
