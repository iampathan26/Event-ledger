package com.mphasis.assignment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.ApiResponse;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Transaction;
import com.mphasis.assignment.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Service", description = "Account Service APIs")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@Operation(summary = "Process Transaction")
	@PostMapping("/{accountId}/transactions")
	public ResponseEntity<ApiResponse<AccountResponseDTO>> processTransaction(@PathVariable String accountId,
			@RequestBody Transaction transaction) {

		logger.info("Processing transaction for account : {}", accountId);

		transaction.setAccountId(accountId);

		AccountResponseDTO response = accountService.processTransaction(transaction);

		ApiResponse<AccountResponseDTO> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Transaction processed successfully");
		apiResponse.setData(response);

		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@Operation(summary = "Get Account Details")
	@GetMapping("/{accountId}")
	public ResponseEntity<ApiResponse<AccountResponseDTO>> getAccount(@PathVariable String accountId) {

		logger.info("Fetching account : {}", accountId);

		AccountResponseDTO response = accountService.getAccount(accountId);

		ApiResponse<AccountResponseDTO> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Account fetched successfully");
		apiResponse.setData(response);

		return ResponseEntity.ok(apiResponse);
	}

	@Operation(summary = "Get Account Balance")
	@GetMapping("/{accountId}/balance")
	public ResponseEntity<ApiResponse<AccountResponseDTO>> getBalance(@PathVariable String accountId) {

		logger.info("Fetching balance for account : {}", accountId);

		AccountResponseDTO response = accountService.getBalance(accountId);

		ApiResponse<AccountResponseDTO> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Balance fetched successfully");
		apiResponse.setData(response);

		return ResponseEntity.ok(apiResponse);
	}

	@Operation(summary = "Get Transaction History")
	@GetMapping("/{accountId}/transactions")
	public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getTransactions(@PathVariable String accountId) {

		logger.info("Fetching transactions for account : {}", accountId);

		List<TransactionResponseDTO> response = accountService.getTransactions(accountId);

		ApiResponse<List<TransactionResponseDTO>> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Transactions fetched successfully");
		apiResponse.setData(response);

		return ResponseEntity.ok(apiResponse);
	}

}