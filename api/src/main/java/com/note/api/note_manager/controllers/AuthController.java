package com.note.api.note_manager.controllers;

import static com.note.api.note_manager.models.user.UserAuthorities.USER;

import com.note.api.note_manager.models.UserInfo;
import com.note.api.note_manager.rest.model.AuthResponse;
import com.note.api.note_manager.rest.model.LoginRequest;
import com.note.api.note_manager.rest.model.RegisterRequest;
import com.note.api.note_manager.rest.model.User;
import com.note.api.note_manager.services.AuthService;
import com.note.api.note_manager.services.UserInfoService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserInfoService userInfoService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/api/v1/auth/login")
  public AuthResponse login(@RequestBody LoginRequest request) {
    var user = userInfoService.getByEmail(request.getEmail());
    var token = authService.loginToToken(request.getEmail(), request.getPassword());

    return new AuthResponse()
        .token(token)
        .user(new User().id(user.getId()).email(user.getEmail()).name(user.getName()));
  }

  @PostMapping("/api/v1/auth/register")
  public AuthResponse register(@RequestBody RegisterRequest request) {
    var userRequest =
        UserInfo.builder()
            .id(UUID.randomUUID().toString())
            .authorities(USER)
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .name(request.getName())
            .build();
    var user = userInfoService.registerUser(userRequest);
    var token = authService.loginToToken(request.getEmail(), request.getPassword());

    return new AuthResponse()
        .token(token)
        .user(new User().id(user.getId()).email(user.getEmail()).name(user.getName()));
  }
}
