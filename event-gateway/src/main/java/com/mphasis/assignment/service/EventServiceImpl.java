package com.mphasis.assignment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.Event;
import com.mphasis.assignment.exception.DuplicateEventException;
import com.mphasis.assignment.exception.EventNotFoundException;
import com.mphasis.assignment.mapper.EventMapper;
import com.mphasis.assignment.metrics.MetricsService;
import com.mphasis.assignment.repository.EventRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventRepository eventRepository;

	private final EventMapper eventMapper;

	private final RestTemplate restTemplate;

	private final MetricsService metricsService;

	@Value("${account.service.url}")
	private String accountServiceUrl;

	public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, RestTemplate restTemplate,
			MetricsService metricsService) {

		this.eventRepository = eventRepository;
		this.eventMapper = eventMapper;
		this.restTemplate = restTemplate;
		this.metricsService = metricsService;
	}

	@Override
	@CircuitBreaker(name = "accountService", fallbackMethod = "fallbackProcess")
	@Retry(name = "accountService")
	public EventResponseDTO createEvent(EventRequestDTO request) {

		logger.info("Processing Event : {}", request.getEventId());

		if (eventRepository.existsByEventId(request.getEventId())) {

			logger.warn("Duplicate Event Received : {}", request.getEventId());

			throw new DuplicateEventException("Duplicate Event Id : " + request.getEventId());
		}

		Event event = eventMapper.toEntity(request);

		logger.info("Saving Event into Database");

		Event savedEvent = eventRepository.save(event);

		// Increment Custom Metric
		metricsService.incrementEventCount();

		String url = accountServiceUrl + "/api/accounts/" + savedEvent.getAccountId() + "/transactions";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Event> httpEntity = new HttpEntity<>(savedEvent, headers);

		try {

			logger.info("Calling Account Service : {}", url);

			restTemplate.postForEntity(url, httpEntity, Object.class);

			logger.info("Account Service updated successfully.");

		} catch (Exception ex) {

			logger.error("Account Service call failed : {}", ex.getMessage());

			throw ex;
		}

		return eventMapper.toResponse(savedEvent);
	}

	public EventResponseDTO fallbackProcess(EventRequestDTO request, Exception ex) {

		logger.error("Fallback executed because Account Service is unavailable.");

		Event event = eventRepository.findById(request.getEventId())
				.orElseThrow(() -> new EventNotFoundException("Event not found with id : " + request.getEventId()));

		return eventMapper.toResponse(event);
	}

	@Override
	@Transactional(readOnly = true)
	public EventResponseDTO getEventById(String eventId) {

		logger.info("Fetching Event : {}", eventId);

		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new EventNotFoundException("Event not found with id : " + eventId));

		return eventMapper.toResponse(event);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventResponseDTO> getEventsByAccountId(String accountId) {

		logger.info("Fetching Events for Account : {}", accountId);

		List<Event> events = eventRepository.findByAccountIdOrderByEventTimestampAsc(accountId);

		return events.stream().map(eventMapper::toResponse).collect(Collectors.toList());
	}

}