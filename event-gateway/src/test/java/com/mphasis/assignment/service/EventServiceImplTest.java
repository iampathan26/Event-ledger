package com.mphasis.assignment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.Event;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.mapper.EventMapper;
import com.mphasis.assignment.metrics.MetricsService;
import com.mphasis.assignment.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

	@Mock
	private EventRepository eventRepository;

	@Mock
	private EventMapper eventMapper;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private MetricsService metricsService;

	@InjectMocks
	private EventServiceImpl eventService;

	private EventRequestDTO request;

	private Event event;

	@BeforeEach
	void setup() {

		ReflectionTestUtils.setField(eventService, "accountServiceUrl", "http://localhost:8081");

		request = new EventRequestDTO();

		request.setEventId("EVT100");
		request.setAccountId("ACC100");
		request.setType(TransactionType.CREDIT);
		request.setAmount(new BigDecimal("5000"));
		request.setCurrency("INR");
		request.setEventTimestamp(Instant.now());
		request.setMetadata("Salary");

		event = new Event();

		event.setEventId(request.getEventId());
		event.setAccountId(request.getAccountId());
		event.setType(request.getType());
		event.setAmount(request.getAmount());
		event.setCurrency(request.getCurrency());
		event.setEventTimestamp(request.getEventTimestamp());
		event.setMetadata(request.getMetadata());
	}

	@Test
	void testCreateEvent() {

		when(eventRepository.existsByEventId("EVT100")).thenReturn(false);

		when(eventMapper.toEntity(request)).thenReturn(event);

		when(eventRepository.save(any(Event.class))).thenReturn(event);

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");
		response.setAccountId("ACC100");

		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponseDTO result = eventService.createEvent(request);

		assertNotNull(result);
		assertEquals("EVT100", result.getEventId());

		verify(eventRepository, times(1)).save(any(Event.class));

		verify(metricsService, times(1)).incrementEventCount();
	}

	@Test
	void testDuplicateEvent() {

		when(eventRepository.existsByEventId("EVT100")).thenReturn(true);

		assertThrows(RuntimeException.class, () -> {

			eventService.createEvent(request);

		});

		verify(eventRepository, never()).save(any(Event.class));
	}

	@Test
	void testGetEventById() {

		when(eventRepository.findById("EVT100")).thenReturn(java.util.Optional.of(event));

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");

		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponseDTO result = eventService.getEventById("EVT100");

		assertEquals("EVT100", result.getEventId());
	}

	@Test
	void testGetEventsByAccountId() {

		when(eventRepository.findByAccountIdOrderByEventTimestampAsc("ACC100")).thenReturn(java.util.List.of(event));

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");

		when(eventMapper.toResponse(event)).thenReturn(response);

		assertEquals(1, eventService.getEventsByAccountId("ACC100").size());
	}

	@Test
	void testFallbackMethod() {

		when(eventRepository.findById("EVT100")).thenReturn(java.util.Optional.of(event));

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");

		when(eventMapper.toResponse(event)).thenReturn(response);

		EventResponseDTO result = eventService.fallbackProcess(request, new RuntimeException("Service Down"));

		assertNotNull(result);
		assertEquals("EVT100", result.getEventId());
	}

}