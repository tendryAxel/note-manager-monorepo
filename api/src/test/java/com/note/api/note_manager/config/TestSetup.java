package com.note.api.note_manager.config;

import com.note.api.note_manager.rest.client.ApiClient;
import com.note.api.note_manager.rest.model.User;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TestSetup {
  @LocalServerPort private int port;
  static Faker faker = new Faker();

  @Container
  static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18")
          .withUsername(faker.name().name())
          .withPassword(faker.credentials().password());

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry register) {
    register.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    register.add("spring.datasource.username", postgreSQLContainer::getUsername);
    register.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }

  private ApiClient anApiClient(Optional<String> token) {
      var apiClient = new ApiClient();
      apiClient.setScheme("http");
      apiClient.setHost("localhost");
      apiClient.setPort(port);

      token.ifPresent(t -> apiClient.setRequestInterceptor(
              builder -> builder.header("Authorization", "Bearer " + t)));

      return apiClient;
  }

  protected ApiClient anApiClient(@NotNull String token) {
    return anApiClient(Optional.of(token));
  }

  protected ApiClient anApiClient() {
    return anApiClient(Optional.empty());
  }

  protected void registerUser(User user, String token) {
  }
}
