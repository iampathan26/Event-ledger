package com.mphasis.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mphasis.assignment.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

	List<Event> findByAccountIdOrderByEventTimestampAsc(String accountId);

	boolean existsByEventId(String eventId);

}