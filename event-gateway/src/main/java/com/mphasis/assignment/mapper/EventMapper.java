package com.mphasis.assignment.mapper;

import org.springframework.stereotype.Component;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;
import com.mphasis.assignment.entity.Event;

@Component
public class EventMapper {

	public Event toEntity(EventRequestDTO dto) {

		if (dto == null) {
			return null;
		}

		Event event = new Event();

		event.setEventId(dto.getEventId());
		event.setAccountId(dto.getAccountId());
		event.setType(dto.getType());
		event.setAmount(dto.getAmount());
		event.setCurrency(dto.getCurrency());
		event.setEventTimestamp(dto.getEventTimestamp());
		event.setMetadata(dto.getMetadata());

		return event;
	}

	public EventResponseDTO toResponse(Event event) {

		if (event == null) {
			return null;
		}

		EventResponseDTO response = new EventResponseDTO();

		response.setEventId(event.getEventId());
		response.setAccountId(event.getAccountId());
		response.setType(event.getType());
		response.setAmount(event.getAmount());
		response.setCurrency(event.getCurrency());
		response.setEventTimestamp(event.getEventTimestamp());
		response.setMetadata(event.getMetadata());

		return response;
	}
}