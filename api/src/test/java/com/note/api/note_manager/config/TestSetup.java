package com.note.api.note_manager.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

class TestSetup {
  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry register) {
    register.add("spring.datasource.url", () -> "url");
    register.add("spring.datasource.username", () -> "username");
    register.add("spring.datasource.password", () -> "password");
  }
}
