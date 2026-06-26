package com.mphasis.assignment.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MetricsService {

	private final Counter transactionCounter;

	public MetricsService(MeterRegistry meterRegistry) {

		this.transactionCounter = Counter.builder("account.transactions.processed")
				.description("Total Account Transactions").register(meterRegistry);
	}

	public void incrementTransactionCount() {
		transactionCounter.increment();
	}
}