package com.mphasis.assignment.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.mphasis.assignment.entity.TransactionType;

public class EventResponseDTO {

	private String eventId;
	private String accountId;
	private TransactionType type;
	private BigDecimal amount;
	private String currency;
	private Instant eventTimestamp;
	private String metadata;

	public EventResponseDTO() {
	}

	public EventResponseDTO(String eventId, String accountId, TransactionType type, BigDecimal amount, String currency,
			Instant eventTimestamp, String metadata) {

		this.eventId = eventId;
		this.accountId = accountId;
		this.type = type;
		this.amount = amount;
		this.currency = currency;
		this.eventTimestamp = eventTimestamp;
		this.metadata = metadata;
	}

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