package com.note.api.note_manager;

import static org.junit.jupiter.api.Assertions.*;

import com.note.api.note_manager.config.TestSetup;
import com.note.api.note_manager.rest.api.AuthenticationApi;
import com.note.api.note_manager.rest.client.ApiException;
import com.note.api.note_manager.rest.model.LoginRequest;
import com.note.api.note_manager.rest.model.RegisterRequest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

public class UserIT extends TestSetup {
  private final Faker faker = new Faker();

  @Test
  void register_ok() throws ApiException {
    var authApi = new AuthenticationApi(anApiClient());
    var registerRequest =
        new RegisterRequest()
            .email(faker.internet().emailAddress())
            .password(faker.credentials().password())
            .name(faker.name().name());

    var registration = authApi.register(registerRequest);

    assertNotNull(registration.getToken());
    assertNotNull(registration.getUser());
    assertNotNull(registration.getUser().getId());
    assertEquals(registerRequest.getEmail(), registration.getUser().getEmail());
    assertEquals(registerRequest.getName(), registration.getUser().getName());
  }

  @Test
  void register_then_login_ok() throws ApiException {
    var authApi = new AuthenticationApi(anApiClient());
    var registerRequest =
        new RegisterRequest()
            .email(faker.internet().emailAddress())
            .password(faker.credentials().password())
            .name(faker.name().name());

    var registration = authApi.register(registerRequest);

    assertNotNull(registration.getToken());
    assertNotNull(registration.getUser());
    assertNotNull(registration.getUser().getId());
    assertEquals(registerRequest.getEmail(), registration.getUser().getEmail());
    assertEquals(registerRequest.getName(), registration.getUser().getName());

    var loginRequest =
        new LoginRequest()
            .email(registerRequest.getEmail())
            .password(registerRequest.getPassword());

    var logged = authApi.login(loginRequest);

    assertNotNull(logged.getUser());
    assertNotNull(logged.getUser().getId());
    assertEquals(registerRequest.getEmail(), logged.getUser().getEmail());
    assertEquals(registerRequest.getName(), logged.getUser().getName());
  }

  @Test
  void login_with_unknow_identifiers_ko() throws ApiException {
    var authApi = new AuthenticationApi(anApiClient());

    var loginRequest =
        new LoginRequest().email("fake.user@gmail.com").password("fake_user_password");

    // TODO: specify the exception
    assertThrows(Exception.class, () -> authApi.login(loginRequest));
  }
}
