package com.note.api.note_manager.controllers;

import com.note.api.note_manager.rest.model.AuthResponse;
import com.note.api.note_manager.rest.model.LoginRequest;
import com.note.api.note_manager.rest.model.User;
import com.note.api.note_manager.services.AuthService;
import com.note.api.note_manager.services.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserInfoService userInfoService;

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        var user = userInfoService.getByEmail(request.getEmail());
        var token = authService.loginToToken(request.getEmail(), request.getPassword());

        return new AuthResponse()
                .token(token)
                .user(new User()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getUsername())
                );
    }
}
