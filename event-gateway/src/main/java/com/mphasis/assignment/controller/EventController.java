package com.mphasis.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mphasis.assignment.dto.ApiResponse;
import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
@Validated
@Tag(name = "Event Gateway API", description = "Event Gateway APIs")
public class EventController {

	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@Operation(summary = "Create Event")
	@PostMapping
	public ResponseEntity<ApiResponse<EventResponseDTO>> createEvent(@Valid @RequestBody EventRequestDTO request) {

		logger.info("Received Event Request : {}", request.getEventId());

		EventResponseDTO response = eventService.createEvent(request);

		ApiResponse<EventResponseDTO> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Event created successfully");
		apiResponse.setData(response);

		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@Operation(summary = "Get Event by Event ID")
	@GetMapping("/{eventId}")
	public ResponseEntity<ApiResponse<EventResponseDTO>> getEventById(@PathVariable String eventId) {

		logger.info("Fetching Event : {}", eventId);

		EventResponseDTO response = eventService.getEventById(eventId);

		ApiResponse<EventResponseDTO> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Event fetched successfully");
		apiResponse.setData(response);

		return ResponseEntity.ok(apiResponse);
	}

	@Operation(summary = "Get Events by Account ID")
	@GetMapping
	public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByAccountId(@RequestParam String accountId) {

		logger.info("Fetching Events for Account : {}", accountId);

		List<EventResponseDTO> response = eventService.getEventsByAccountId(accountId);

		ApiResponse<List<EventResponseDTO>> apiResponse = new ApiResponse<>();

		apiResponse.setSuccess(true);
		apiResponse.setMessage("Events fetched successfully");
		apiResponse.setData(response);

		return ResponseEntity.ok(apiResponse);
	}

}