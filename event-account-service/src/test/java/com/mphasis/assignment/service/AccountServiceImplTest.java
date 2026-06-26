package com.mphasis.assignment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Account;
import com.mphasis.assignment.entity.Transaction;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.mapper.AccountMapper;
import com.mphasis.assignment.repository.AccountRepository;
import com.mphasis.assignment.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountMapper accountMapper;

	@InjectMocks
	private AccountServiceImpl accountService;

	private Account account;

	private Transaction transaction;

	@BeforeEach
	void setup() {

		account = new Account();

		account.setAccountId("ACC100");
		account.setBalance(new BigDecimal("1000"));
		account.setCurrency("INR");

		transaction = new Transaction();

		transaction.setEventId("EVT100");
		transaction.setAccountId("ACC100");
		transaction.setType(TransactionType.CREDIT);
		transaction.setAmount(new BigDecimal("500"));
		transaction.setCurrency("INR");
		transaction.setEventTimestamp(Instant.now());
		transaction.setMetadata("Salary Credit");
	}

	@Test
	void testProcessTransaction() {

		when(transactionRepository.existsByEventId("EVT100")).thenReturn(false);

		when(accountRepository.findById("ACC100")).thenReturn(java.util.Optional.of(account));

		when(accountRepository.save(any(Account.class))).thenReturn(account);

		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("1500"));
		response.setCurrency("INR");

		when(accountMapper.toAccountResponse(any(Account.class))).thenReturn(response);

		AccountResponseDTO result = accountService.processTransaction(transaction);

		assertNotNull(result);
		assertEquals("ACC100", result.getAccountId());
		assertEquals(new BigDecimal("1500"), result.getBalance());

		verify(accountRepository, times(1)).save(any(Account.class));

		verify(transactionRepository, times(1)).save(any(Transaction.class));
	}

	@Test
	void testGetAccount() {

		when(accountRepository.findById("ACC100")).thenReturn(java.util.Optional.of(account));

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("1000"));
		response.setCurrency("INR");

		when(accountMapper.toAccountResponse(account)).thenReturn(response);

		AccountResponseDTO result = accountService.getAccount("ACC100");

		assertNotNull(result);
		assertEquals("ACC100", result.getAccountId());
	}

	@Test
	void testGetBalance() {

		when(accountRepository.findById("ACC100")).thenReturn(java.util.Optional.of(account));

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("1000"));
		response.setCurrency("INR");

		when(accountMapper.toAccountResponse(account)).thenReturn(response);

		AccountResponseDTO result = accountService.getBalance("ACC100");

		assertEquals(new BigDecimal("1000"), result.getBalance());
	}

	@Test
	void testGetTransactions() {

		when(transactionRepository.findByAccountIdOrderByEventTimestampAsc("ACC100"))
				.thenReturn(java.util.List.of(transaction));

		TransactionResponseDTO dto = new TransactionResponseDTO();

		dto.setEventId("EVT100");
		dto.setAccountId("ACC100");

		when(accountMapper.toTransactionResponse(transaction)).thenReturn(dto);

		assertEquals(1, accountService.getTransactions("ACC100").size());
	}

	@Test
	void testDuplicateTransaction() {

		when(transactionRepository.existsByEventId("EVT100")).thenReturn(true);

		when(accountRepository.findById("ACC100")).thenReturn(java.util.Optional.of(account));

		AccountResponseDTO response = new AccountResponseDTO();

		response.setAccountId("ACC100");
		response.setBalance(new BigDecimal("1000"));

		when(accountMapper.toAccountResponse(account)).thenReturn(response);

		AccountResponseDTO result = accountService.processTransaction(transaction);

		assertNotNull(result);

		verify(accountRepository, never()).save(any(Account.class));
	}

}