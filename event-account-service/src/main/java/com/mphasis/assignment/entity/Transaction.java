package com.mphasis.assignment.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String eventId;

	@Column(nullable = false)
	private String accountId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String currency;

	@Column(nullable = false)
	private Instant eventTimestamp;

	private String metadata;

	public Transaction() {
	}

	public Transaction(Long id, String eventId, String accountId, TransactionType type, BigDecimal amount,
			String currency, Instant eventTimestamp, String metadata) {

		this.id = id;
		this.eventId = eventId;
		this.accountId = accountId;
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.eventTimestamp = eventTimestamp;
		this.metadata = metadata;
	}

	public Long getId() {
		return id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Instant getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(Instant eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
}