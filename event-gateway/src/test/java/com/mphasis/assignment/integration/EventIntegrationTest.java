package com.mphasis.assignment.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.service.EventService;

@SpringBootTest
class EventIntegrationTest {

	@Autowired
	private EventService eventService;

	@Test
	void testCreateEventIntegration() {

		EventRequestDTO request = new EventRequestDTO();

		request.setEventId("EVT999");
		request.setAccountId("ACC999");
		request.setType(TransactionType.CREDIT);
		request.setAmount(new BigDecimal("1000"));
		request.setCurrency("INR");
		request.setEventTimestamp(Instant.now());
		request.setMetadata("Integration Test");

		EventResponseDTO response = eventService.createEvent(request);

		assertNotNull(response);
		assertEquals("EVT999", response.getEventId());
	}

}