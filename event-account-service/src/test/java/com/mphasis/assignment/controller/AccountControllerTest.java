package com.mphasis.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Transaction;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.service.AccountService;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testProcessTransaction() throws Exception {

		Transaction transaction = new Transaction();

		transaction.setEventId("EVT100");
		transaction.setAccountId("ACC100");
		transaction.setType(TransactionType.CREDIT);
		transaction.setAmount(new BigDecimal("5000"));
		transaction.setCurrency("INR");
		transaction.setEventTimestamp(Instant.now());
		transaction.setMetadata("Salary");

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("5000"));
		response.setCurrency("INR");

		when(accountService.processTransaction(any(Transaction.class))).thenReturn(response);

		mockMvc.perform(post("/api/accounts/ACC100/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaction))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.accountId").value("ACC100"))
				.andExpect(jsonPath("$.data.balance").value(5000));
	}

	@Test
	void testGetAccount() throws Exception {

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("5000"));
		response.setCurrency("INR");

		when(accountService.getAccount("ACC100")).thenReturn(response);

		mockMvc.perform(get("/api/accounts/ACC100")).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.accountId").value("ACC100"))
				.andExpect(jsonPath("$.data.balance").value(5000));
	}

	@Test
	void testGetBalance() throws Exception {

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("5000"));
		response.setCurrency("INR");

		when(accountService.getBalance("ACC100")).thenReturn(response);

		mockMvc.perform(get("/api/accounts/ACC100/balance")).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.balance").value(5000));
	}

	@Test
	void testGetTransactions() throws Exception {

		TransactionResponseDTO transaction = new TransactionResponseDTO();

		transaction.setEventId("EVT100");
		transaction.setAccountId("ACC100");
		transaction.setType(TransactionType.CREDIT);
		transaction.setAmount(new BigDecimal("5000"));
		transaction.setCurrency("INR");
		transaction.setEventTimestamp(Instant.now());
		transaction.setMetadata("Salary");

		when(accountService.getTransactions("ACC100")).thenReturn(java.util.Arrays.asList(transaction));

		mockMvc.perform(get("/api/accounts/ACC100/transactions")).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].eventId").value("EVT100"))
				.andExpect(jsonPath("$.data[0].accountId").value("ACC100"));
	}

}