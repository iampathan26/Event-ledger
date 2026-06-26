package com.mphasis.assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.TransactionType;
import com.mphasis.assignment.service.EventService;

@WebMvcTest(EventController.class)
class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventService eventService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateEvent() throws Exception {

		EventRequestDTO request = new EventRequestDTO();

		request.setEventId("EVT100");
		request.setAccountId("ACC100");
		request.setType(TransactionType.CREDIT);
		request.setAmount(new BigDecimal("5000"));
		request.setCurrency("INR");
		request.setEventTimestamp(Instant.now());
		request.setMetadata("Salary");

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");
		response.setAccountId("ACC100");
		response.setType(TransactionType.CREDIT);
		response.setAmount(new BigDecimal("5000"));
		response.setCurrency("INR");
		response.setEventTimestamp(request.getEventTimestamp());
		response.setMetadata("Salary");

		when(eventService.createEvent(any(EventRequestDTO.class))).thenReturn(response);

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.eventId").value("EVT100"));
	}

	@Test
	void testGetEventById() throws Exception {

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");
		response.setAccountId("ACC100");
		response.setType(TransactionType.CREDIT);
		response.setAmount(new BigDecimal("5000"));
		response.setCurrency("INR");
		response.setEventTimestamp(Instant.now());
		response.setMetadata("Salary");

		when(eventService.getEventById("EVT100")).thenReturn(response);

		mockMvc.perform(get("/api/events/EVT100")).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.eventId").value("EVT100"));
	}

	@Test
	void testGetEventsByAccountId() throws Exception {

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId("EVT100");
		response.setAccountId("ACC100");
		response.setType(TransactionType.CREDIT);
		response.setAmount(new BigDecimal("5000"));
		response.setCurrency("INR");
		response.setEventTimestamp(Instant.now());
		response.setMetadata("Salary");

		when(eventService.getEventsByAccountId("ACC100")).thenReturn(List.of(response));

		mockMvc.perform(get("/api/events").param("accountId", "ACC100")).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].eventId").value("EVT100"))
				.andExpect(jsonPath("$.data[0].accountId").value("ACC100"));
	}

}
