package com.mphasis.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mphasis.assignment.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleAccountNotFound(AccountNotFoundException ex) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(false);
		response.setMessage(ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateTransactionException.class)
	public ResponseEntity<ApiResponse<Object>> handleDuplicateTransaction(DuplicateTransactionException ex) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(false);
		response.setMessage(ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ApiResponse<Object>> handleInsufficientBalance(InsufficientBalanceException ex) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(false);
		response.setMessage(ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(false);
		response.setMessage(ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}