package com.mphasis.assignment.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {

		Map<String, Object> response = new HashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.NOT_FOUND.value());
		response.put("error", "Not Found");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateEventException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateEvent(DuplicateEventException ex) {

		Map<String, Object> response = new HashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.CONFLICT.value());
		response.put("error", "Conflict");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidEventException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidEvent(InvalidEventException ex) {

		Map<String, Object> response = new HashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Bad Request");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		Map<String, Object> response = new HashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Validation Failed");
		response.put("message", errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex) {

		Map<String, Object> response = new HashMap<>();

		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put("error", "Internal Server Error");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}