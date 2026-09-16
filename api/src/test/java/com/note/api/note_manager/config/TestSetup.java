package com.note.api.note_manager.config;

import net.datafaker.Faker;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@Testcontainers
public class TestSetup {
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
}
