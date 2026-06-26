package com.mphasis.assignment.metrics;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class MetricsService {

	private final Counter eventCounter;

	public MetricsService(MeterRegistry registry) {

		eventCounter = Counter.builder("event.processed.count").description("Total Processed Events")
				.register(registry);
	}

	public void incrementEventCount() {

		eventCounter.increment();
	}
}