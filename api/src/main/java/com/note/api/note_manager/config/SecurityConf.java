package com.note.api.note_manager.config;

import static org.springframework.http.HttpMethod.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConf {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private final AuthProvider authProvider;
  private final HandlerExceptionResolver exceptionResolver;

  public SecurityConf(
      AuthProvider authProvider,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.authProvider = authProvider;
    this.exceptionResolver = exceptionResolver;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private BearerAuthFilter bearerFilter(RequestMatcher requestMatcher) {
    var authFilter = new BearerAuthFilter(requestMatcher, AUTHORIZATION_HEADER);
    authFilter.setAuthenticationManager(authenticationManager());

    authFilter.setAuthenticationSuccessHandler(
        (HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) -> {});
    authFilter.setAuthenticationFailureHandler(
        (HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException) -> {
          exceptionResolver.resolveException(
              request, response, null, forbiddenWithRemoteInfo(request));
        });

    return authFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterBefore(
            bearerFilter(
                new OrRequestMatcher(
                    PathPatternRequestMatcher.withDefaults().matcher(GET, "/api/v1/notes"),
                    PathPatternRequestMatcher.withDefaults().matcher(POST, "/api/v1/notes"))),
            AnonymousAuthenticationFilter.class)
        .authenticationProvider(authProvider)
        .exceptionHandling(
            exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer
                    .authenticationEntryPoint(
                        // note(spring-exception)
                        // https://stackoverflow.com/questions/59417122/how-to-handle-usernamenotfoundexception-spring-security
                        // issues like when a user tries to access a resource
                        // without appropriate authentication elements
                        (req, res, e) ->
                            exceptionResolver.resolveException(
                                req, res, null, forbiddenWithRemoteInfo(req)))
                    .accessDeniedHandler(
                        // note(spring-exception): issues like when a user not having required roles
                        (req, res, e) ->
                            exceptionResolver.resolveException(
                                req, res, null, forbiddenWithRemoteInfo(req))))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(OPTIONS, "/**")
                    .permitAll()
                    // NOTE
                    .requestMatchers(GET, "/api/v1/notes")
                    .authenticated()
                    .requestMatchers(POST, "/api/v1/notes")
                    .authenticated()
                    // AUTH
                    .requestMatchers(POST, "/api/v1/auth/login")
                    .permitAll()
                    .requestMatchers(POST, "/api/v1/auth/register")
                    .permitAll()
                    .anyRequest()
                    .denyAll())
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(authProvider);
  }

  private Exception forbiddenWithRemoteInfo(HttpServletRequest req) {
    log.info(
        String.format(
            "Access is denied for remote caller: address=%s, host=%s, port=%s",
            req.getRemoteAddr(), req.getRemoteHost(), req.getRemotePort()));
    return new RuntimeException("Access is denied");
  }
}
