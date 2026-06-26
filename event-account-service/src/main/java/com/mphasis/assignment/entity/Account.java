package com.mphasis.assignment.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@Column(name = "account_id")
	private String accountId;

	@Column(nullable = false)
	private BigDecimal balance;

	@Column(nullable = false)
	private String currency;

	public Account() {
	}

	public Account(String accountId, BigDecimal balance, String currency) {
		this.accountId = accountId;
		this.balance = balance;
		this.currency = currency;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}