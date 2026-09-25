package com.note.api.note_manager.config;

import static java.util.Optional.empty;

import com.note.api.note_manager.models.UserInfo;
import com.note.api.note_manager.services.SessionTokenService;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthProvider extends AbstractUserDetailsAuthenticationProvider {
  private final SessionTokenService sessionTokenService;
  private static final String BEARER_PREFIX = "Bearer ";

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

  public static UserInfo getPrincipal() {
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    Object principal = authentication.getPrincipal();
    return (UserInfo) principal;
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
      throw new AuthenticationCredentialsNotFoundException("Request don't contains bearer token");
    }
    var userHasTokenStillValid = sessionTokenService.findUserHasTokenStillValid(bearerToken.get());
    if (userHasTokenStillValid.isEmpty()) {
      throw new SessionAuthenticationException("Invalid Token " + bearerToken.get());
    }
    return userHasTokenStillValid.get();
  }
}
