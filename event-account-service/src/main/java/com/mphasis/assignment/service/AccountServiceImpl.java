package com.mphasis.assignment.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mphasis.assignment.dto.AccountResponseDTO;
import com.mphasis.assignment.dto.TransactionResponseDTO;
import com.mphasis.assignment.entity.Account;
import com.mphasis.assignment.entity.Transaction;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.mapper.AccountMapper;
import com.mphasis.assignment.repository.AccountRepository;
import com.mphasis.assignment.repository.TransactionRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final AccountMapper accountMapper;

	public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
			AccountMapper accountMapper) {

		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.accountMapper = accountMapper;
	}

	@Override
	public AccountResponseDTO processTransaction(Transaction transaction) {

		logger.info("Processing transaction : {}", transaction.getEventId());

		if (transactionRepository.existsByEventId(transaction.getEventId())) {

			logger.warn("Duplicate transaction ignored : {}", transaction.getEventId());

			Account account = accountRepository.findById(transaction.getAccountId())
					.orElseThrow(() -> new RuntimeException("Account not found"));

			return accountMapper.toAccountResponse(account);
		}

		Account account = accountRepository.findById(transaction.getAccountId())
				.orElse(new Account(transaction.getAccountId(), BigDecimal.ZERO, transaction.getCurrency()));

		calculateBalance(account, transaction);

		accountRepository.save(account);

		transactionRepository.save(transaction);

		logger.info("Updated balance : {}", account.getBalance());

		return accountMapper.toAccountResponse(account);
	}

	private void calculateBalance(Account account, Transaction transaction) {

		if (transaction.getType() == TransactionType.CREDIT) {

			account.setBalance(account.getBalance().add(transaction.getAmount()));

		} else {

			if (account.getBalance().compareTo(transaction.getAmount()) < 0) {

				throw new RuntimeException("Insufficient balance for account : " + account.getAccountId());
			}

			account.setBalance(account.getBalance().subtract(transaction.getAmount()));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public AccountResponseDTO getAccount(String accountId) {

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found : " + accountId));

		return accountMapper.toAccountResponse(account);
	}

	@Override
	@Transactional(readOnly = true)
	public AccountResponseDTO getBalance(String accountId) {

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new RuntimeException("Account not found : " + accountId));

		return accountMapper.toAccountResponse(account);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getTransactions(String accountId) {

		return transactionRepository.findByAccountIdOrderByEventTimestampAsc(accountId).stream()
				.map(accountMapper::toTransactionResponse).collect(Collectors.toList());
	}

}