package com.note.api.note_manager.config;

import com.note.api.note_manager.rest.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class InternalToRestExceptionHandler {
  @ExceptionHandler(value = {Exception.class})
  ResponseEntity<ErrorResponse> handleDefault(Exception e) {
    log.error("Internal error", e);
    return new ResponseEntity<>(
        toRest(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse toRest(Exception e, HttpStatus status) {
    var restException = new ErrorResponse();
    restException.setCode(status.toString());
    restException.setMessage(e.getMessage());
    return restException;
  }
}
