package com.note.api.note_manager.services;

import com.note.api.note_manager.utils.date.DateTypeEnum;
import com.note.api.note_manager.utils.date.DateUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;

import static com.note.api.note_manager.utils.date.DateTypeEnum.DAY;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
@AllArgsConstructor
public class JwtServices {
    private final DateUtils dateUtils;
    // TODO: deprecated
    private final SecretKey key = Keys.secretKeyFor(HS256);

    public String generateToken(UserDetails userDetails) {
        var now = new Date();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(dateUtils.add(now, DAY, 1))
                .signWith(key)
                .compact();
    }
}
