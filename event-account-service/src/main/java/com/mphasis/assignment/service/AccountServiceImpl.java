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
import com.mphasis.assignment.exception.AccountNotFoundException;
import com.mphasis.assignment.exception.DuplicateTransactionException;
import com.mphasis.assignment.exception.InsufficientBalanceException;
import com.mphasis.assignment.mapper.AccountMapper;
import com.mphasis.assignment.metrics.MetricsService;
import com.mphasis.assignment.repository.AccountRepository;
import com.mphasis.assignment.repository.TransactionRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private final AccountRepository accountRepository;

	private final TransactionRepository transactionRepository;

	private final AccountMapper accountMapper;

	private final MetricsService metricsService;

	public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository,
			AccountMapper accountMapper, MetricsService metricsService) {

		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.accountMapper = accountMapper;
		this.metricsService = metricsService;
	}

	@Override
	public AccountResponseDTO processTransaction(Transaction transaction) {

		logger.info("Processing transaction for account : {}", transaction.getAccountId());

		// Check duplicate transaction
		if (transactionRepository.existsByEventId(transaction.getEventId())) {

			logger.error("Duplicate transaction detected : {}", transaction.getEventId());

			throw new DuplicateTransactionException(
					"Transaction already processed with Event Id : " + transaction.getEventId());
		}

		// Fetch account or create a new one
		Account account = accountRepository.findById(transaction.getAccountId()).orElseGet(() -> {

			logger.info("Account not found. Creating new account : {}", transaction.getAccountId());

			Account newAccount = new Account();
			newAccount.setAccountId(transaction.getAccountId());
			newAccount.setBalance(BigDecimal.ZERO);
			newAccount.setCurrency(transaction.getCurrency());

			return newAccount;
		});

		// Process CREDIT transaction
		if (TransactionType.CREDIT.equals(transaction.getType())) {

			account.setBalance(account.getBalance().add(transaction.getAmount()));

		}
		// Process DEBIT transaction
		else {

			if (account.getBalance().compareTo(transaction.getAmount()) < 0) {

				logger.error("Insufficient balance for account : {}", transaction.getAccountId());

				throw new InsufficientBalanceException(
						"Insufficient balance for account : " + transaction.getAccountId());
			}

			account.setBalance(account.getBalance().subtract(transaction.getAmount()));
		}

		logger.info("Saving account details.");

		Account savedAccount = accountRepository.save(account);

		logger.info("Saving transaction details.");

		transactionRepository.save(transaction);

		// Increment Prometheus Metric
		metricsService.incrementTransactionCount();

		logger.info("Transaction processed successfully.");

		return accountMapper.toAccountResponse(savedAccount);
	}

	@Override
	@Transactional(readOnly = true)
	public AccountResponseDTO getAccount(String accountId) {

		logger.info("Fetching account details for account : {}", accountId);

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found : " + accountId));

		return accountMapper.toAccountResponse(account);
	}

	@Override
	@Transactional(readOnly = true)
	public AccountResponseDTO getBalance(String accountId) {

		logger.info("Fetching balance for account : {}", accountId);

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found : " + accountId));

		return accountMapper.toAccountResponse(account);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getTransactions(String accountId) {

		logger.info("Fetching transaction history for account : {}", accountId);

		List<Transaction> transactions = transactionRepository.findByAccountIdOrderByEventTimestampAsc(accountId);

		return transactions.stream().map(accountMapper::toTransactionResponse).collect(Collectors.toList());
	}

}