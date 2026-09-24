package com.note.api.note_manager.config.container;

import net.datafaker.Faker;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.postgresql.PostgreSQLContainer;

public class PostgresSetup {
  private final Faker faker;
  private final PostgreSQLContainer container;

  public PostgresSetup() {
    this.faker = new Faker();
    this.container =
        new PostgreSQLContainer("postgres:18")
            .withUsername(faker.name().name())
            .withPassword(faker.credentials().password());
  }

  public void start() {
    container.start();
  }

  public void stop() {
    container.stop();
  }

  public void configure(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }
}
