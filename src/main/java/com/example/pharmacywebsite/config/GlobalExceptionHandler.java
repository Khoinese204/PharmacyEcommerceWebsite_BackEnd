package com.example.pharmacywebsite.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Có thể thêm nhiều handler khác tại đây nếu muốn:
    /*
     * @ExceptionHandler(EntityNotFoundException.class)
     * public ResponseEntity<Map<String, Object>>
     * handleNotFound(EntityNotFoundException ex) {
     * Map<String, Object> error = new HashMap<>();
     * error.put("timestamp", LocalDateTime.now());
     * error.put("status", HttpStatus.NOT_FOUND.value());
     * error.put("message", ex.getMessage());
     * return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
     * }
     */
}
