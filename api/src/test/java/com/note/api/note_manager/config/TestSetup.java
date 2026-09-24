package com.note.api.note_manager.config;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.note.api.note_manager.config.container.PostgresSetup;
import com.note.api.note_manager.rest.api.AuthenticationApi;
import com.note.api.note_manager.rest.client.ApiClient;
import com.note.api.note_manager.rest.client.ApiException;
import com.note.api.note_manager.rest.model.AuthResponse;
import com.note.api.note_manager.rest.model.RegisterRequest;
import java.util.Optional;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestSetup {
  @LocalServerPort private int port;
  private static PostgresSetup postgresSetup = new PostgresSetup();

  @BeforeAll
  static void setUp() {
    postgresSetup.start();
    Runtime.getRuntime().addShutdownHook(new Thread(postgresSetup::stop));
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry register) {
    postgresSetup.configure(register);
  }

  private ApiClient anApiClient(Optional<String> token) {
    var apiClient = new ApiClient();
    apiClient.setScheme("http");
    apiClient.setHost("localhost");
    apiClient.setPort(port);

    token.ifPresent(
        t ->
            apiClient.setRequestInterceptor(
                builder -> builder.header("Authorization", "Bearer " + t)));

    return apiClient;
  }

  protected ApiClient anApiClient(@NotNull String token) {
    return anApiClient(Optional.of(token));
  }

  protected ApiClient anApiClient() {
    return anApiClient(Optional.empty());
  }

  protected AuthResponse registerUser(RegisterRequest user) {
    var authApi = new AuthenticationApi(anApiClient());

    try {
      return authApi.register(user);
    } catch (ApiException e) {
      throw new RuntimeException("User cannot be registered " + e);
    }
  }

  protected AuthResponse registerUser() {
    var faker = new Faker();
    return registerUser(
        new RegisterRequest()
            .email(faker.internet().emailAddress())
            .name(faker.name().name())
            .password(faker.credentials().password()));
  }
}
