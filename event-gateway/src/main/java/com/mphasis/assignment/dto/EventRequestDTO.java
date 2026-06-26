package com.mphasis.assignment.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.mphasis.assignment.entity.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EventRequestDTO {

	@NotBlank(message = "Event ID is required")
	private String eventId;

	@NotBlank(message = "Account ID is required")
	private String accountId;

	@NotNull(message = "Transaction Type is required")
	private TransactionType type;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than zero")
	private BigDecimal amount;

	@NotBlank(message = "Currency is required")
	private String currency;

	@NotNull(message = "Event Timestamp is required")
	private Instant eventTimestamp;

	private String metadata;

	// Default Constructor
	public EventRequestDTO() {
	}

	// Parameterized Constructor
	public EventRequestDTO(String eventId, String accountId, TransactionType type, BigDecimal amount, String currency,
			Instant eventTimestamp, String metadata) {

		this.eventId = eventId;
		this.accountId = accountId;
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.eventTimestamp = eventTimestamp;
		this.metadata = metadata;
	}

	// Getters and Setters

	public String getEventId() {
		return eventId;
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