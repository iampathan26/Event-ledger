package com.mphasis.assignment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.Event;
import com.mphasis.assignment.exception.DuplicateEventException;
import com.mphasis.assignment.exception.EventNotFoundException;
import com.mphasis.assignment.mapper.EventMapper;
import com.mphasis.assignment.metrics.MetricsService;
import com.mphasis.assignment.queue.FallbackQueueService;
import com.mphasis.assignment.repository.EventRepository;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventRepository eventRepository;

	private final EventMapper eventMapper;

	private final RestTemplate restTemplate;

	private final MetricsService metricsService;

	private final FallbackQueueService fallbackQueueService;

	@Value("${account.service.url}")
	private String accountServiceUrl;

	public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, RestTemplate restTemplate,
			MetricsService metricsService, FallbackQueueService fallbackQueueService) {

		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
		this.restTemplate = restTemplate;
		this.metricsService = metricsService;
		this.fallbackQueueService = fallbackQueueService;
	}

	@Override
	@CircuitBreaker(name = "accountService", fallbackMethod = "fallbackProcess")
	@Retry(name = "accountService")
	public EventResponseDTO createEvent(EventRequestDTO request) {

		logger.info("Received event : {}", request.getEventId());

		if (eventRepository.existsByEventId(request.getEventId())) {

			logger.error("Duplicate Event Id : {}", request.getEventId());

			throw new DuplicateEventException("Event already exists with id : " + request.getEventId());
		}

		Event event = eventMapper.toEntity(request);

		logger.info("Saving event into database.");

		Event savedEvent = eventRepository.save(event);

		metricsService.incrementEventCount();

		logger.info("Calling Account Service.");

		try {

			restTemplate.postForEntity(accountServiceUrl + "/api/accounts/" + event.getAccountId() + "/transactions",
					savedEvent, Void.class);

			logger.info("Account Service processed successfully.");

		} catch (Exception ex) {

			logger.error("Account Service unavailable : {}", ex.getMessage());

			logger.warn("Adding event into asynchronous fallback queue.");

			fallbackQueueService.add(request);
		}

		logger.info("Event processed successfully.");

		return eventMapper.toResponse(savedEvent);
	}

	public EventResponseDTO fallbackProcess(EventRequestDTO request, Exception ex) {

		logger.error("Circuit Breaker activated. Reason : {}", ex.getMessage());

		logger.warn("Adding event {} to fallback queue.", request.getEventId());

		fallbackQueueService.add(request);

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId(request.getEventId());
		response.setAccountId(request.getAccountId());
		response.setType(request.getType());
		response.setAmount(request.getAmount());
		response.setCurrency(request.getCurrency());
		response.setEventTimestamp(request.getEventTimestamp());
		response.setMetadata(request.getMetadata());

		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public EventResponseDTO getEventById(String eventId) {

		logger.info("Fetching event : {}", eventId);

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new EventNotFoundException("Event not found : " + eventId));

		return eventMapper.toResponse(event);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDTO> getEventsByAccountId(String accountId) {

		logger.info("Fetching events for account : {}", accountId);

		List<Event> events = eventRepository.findByAccountIdOrderByEventTimestampAsc(accountId);

		return events.stream().map(eventMapper::toResponse).collect(Collectors.toList());
	}

}