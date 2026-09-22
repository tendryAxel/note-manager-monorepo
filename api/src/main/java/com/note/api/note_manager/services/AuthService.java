package com.note.api.note_manager.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;

    public String loginToToken(String userName, String password) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        var userDetails =
                (UserDetails) authentication.getPrincipal();
        return jwtServices.generateToken(userDetails);
    }
}
