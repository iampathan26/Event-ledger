package com.mphasis.assignment.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class EventGatewayHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {

		return Health.up().withDetail("Service", "Event Gateway").withDetail("Status", "Running").build();
	}
}