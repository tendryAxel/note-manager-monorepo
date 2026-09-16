package com.note.api.note_manager.config.container;

class PostgresContainer {
  private final PostgreSQLContainer<?> postgresContainer;

  public PostgresContainer() {
    postgresContainer = new PostgreSQLContainer<>();
  }
}
