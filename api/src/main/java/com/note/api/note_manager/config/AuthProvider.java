package com.note.api.note_manager.config;

import static java.util.Optional.empty;

import com.note.api.note_manager.models.SessionToken;
import com.note.api.note_manager.services.UserInfoService;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthProvider extends AbstractUserDetailsAuthenticationProvider {
  private final UserInfoService userInfoService;
  private static final String BEARER_PREFIX = "bearer ";

  private Optional<String> getBearer(UsernamePasswordAuthenticationToken authenticationToken) {
    Object credentials = authenticationToken.getCredentials();
    if (!(credentials instanceof String) || !((String) credentials).startsWith(BEARER_PREFIX)) {
      return empty();
    }
    String token = ((String) credentials).substring(BEARER_PREFIX.length()).trim();
    return isJwtShaped(token) ? Optional.of(token) : empty();
  }

  private static boolean isJwtShaped(String token) {
    String[] parts = token.split("\\.", -1);
    return parts.length == 3 && Arrays.stream(parts).noneMatch(String::isBlank);
  }

  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {}

  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    var bearerToken = getBearer(authentication);
    if (bearerToken.isEmpty()) {
      throw new RuntimeException("Request don't contains bearer token");
    }
    var user = userInfoService.getByEmail(username);
    for (SessionToken sessionToken : user.getValidSessionToken()) {
      if (sessionToken.getToken().equals(bearerToken.get())) {
        return user;
      }
    }
    throw new RuntimeException("Invalid Token");
  }
}
