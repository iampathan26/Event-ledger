package com.mphasis.assignment.service;

import java.util.List;

import com.mphasis.assignment.dto.EventRequestDTO;
import com.mphasis.assignment.dto.EventResponseDTO;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO request);

    EventResponseDTO getEventById(String eventId);

    List<EventResponseDTO> getEventsByAccountId(String accountId);

}