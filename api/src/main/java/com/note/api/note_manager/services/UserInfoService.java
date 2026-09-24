package com.note.api.note_manager.services;

import com.note.api.note_manager.models.UserInfo;
import com.note.api.note_manager.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userInfoRepository
                .findUserInfoByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s not found".formatted(email)));
    }

    public UserInfo getByEmail(String email) throws RuntimeException {
        return userInfoRepository
                .findUserInfoByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email %s not found".formatted(email)));
    }

    public UserInfo registerUser(UserInfo user) {
        return userInfoRepository.save(user);
    }
}
