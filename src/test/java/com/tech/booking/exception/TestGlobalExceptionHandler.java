package com.tech.booking.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class TestGlobalExceptionHandler {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntime_shouldReturnInternalServerError() {
        RuntimeException ex = new RuntimeException("Test runtime error");
        ResponseEntity<String> response = handler.handleRuntime(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Test runtime error");
    }

    @Test
    void handleResourceAlreadyExists_shouldReturnConflictWithErrorMessage() {
        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("Already exists");
        ResponseEntity<?> response = handler.handleResourceAlreadyExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isInstanceOfAny(java.util.Map.class);
        assertThat(((java.util.Map<?, ?>) response.getBody()).get("error")).isEqualTo("Already exists");
    }

    @Test
    void handleResourceNotFound_shouldReturnNotFoundWithErrorMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<?> response = handler.handleResourceNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isInstanceOfAny(java.util.Map.class);
        assertThat(((java.util.Map<?, ?>) response.getBody()).get("error")).isEqualTo("Not found");
    }
}