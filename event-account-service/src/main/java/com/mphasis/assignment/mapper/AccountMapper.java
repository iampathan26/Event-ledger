package com.mphasis.assignment.mapper;

import org.springframework.stereotype.Component;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Account;
import com.mphasis.assignment.entity.Transaction;

@Component
public class AccountMapper {

	public AccountResponseDTO toAccountResponse(Account account) {

		if (account == null) {
			return null;
		}

		AccountResponseDTO dto = new AccountResponseDTO();

		dto.setAccountId(account.getAccountId());
		dto.setBalance(account.getBalance());
		dto.setCurrency(account.getCurrency());

		return dto;
	}

	public TransactionResponseDTO toTransactionResponse(Transaction transaction) {

		if (transaction == null) {
			return null;
		}

		TransactionResponseDTO dto = new TransactionResponseDTO();

		dto.setId(transaction.getId());
		dto.setEventId(transaction.getEventId());
		dto.setAccountId(transaction.getAccountId());
		dto.setType(transaction.getType());
		dto.setAmount(transaction.getAmount());
		dto.setCurrency(transaction.getCurrency());
		dto.setEventTimestamp(transaction.getEventTimestamp());
		dto.setMetadata(transaction.getMetadata());

		return dto;
	}

}