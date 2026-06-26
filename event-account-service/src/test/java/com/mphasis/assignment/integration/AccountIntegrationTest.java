package com.mphasis.assignment.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.entity.Transaction;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.service.AccountService;

@SpringBootTest
class AccountIntegrationTest {

	@Autowired
	private AccountService accountService;

	@Test
	void testProcessTransactionIntegration() {

		Transaction transaction = new Transaction();

		transaction.setEventId("EVT999");
		transaction.setAccountId("ACC999");
		transaction.setType(TransactionType.CREDIT);
		transaction.setAmount(new BigDecimal("1000"));
		transaction.setCurrency("INR");
		transaction.setEventTimestamp(Instant.now());
		transaction.setMetadata("Integration Test");

		AccountResponseDTO response = accountService.processTransaction(transaction);

		assertNotNull(response);
		assertEquals("ACC999", response.getAccountId());
	}

}