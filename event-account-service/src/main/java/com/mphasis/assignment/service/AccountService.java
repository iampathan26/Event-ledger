package com.mphasis.assignment.service;

import java.util.List;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Transaction;

public interface AccountService {

	/**
	 * Process an incoming transaction from Event Gateway.
	 */
	AccountResponseDTO processTransaction(Transaction transaction);

	/**
	 * Get account details.
	 */
	AccountResponseDTO getAccount(String accountId);

	/**
	 * Get current account balance.
	 */
	AccountResponseDTO getBalance(String accountId);

	/**
	 * Get transaction history.
	 */
	List<TransactionResponseDTO> getTransactions(String accountId);

}