package com.mphasis.assignment.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mphasis.assignment.dto.EventRequestDTO;

@Service
public class FallbackQueueService {

	private static final Logger logger = LoggerFactory.getLogger(FallbackQueueService.class);

	private final BlockingQueue<EventRequestDTO> queue = new LinkedBlockingQueue<>();

	private final RestTemplate restTemplate;

	@Value("${account.service.url}")
	private String accountServiceUrl;

	public FallbackQueueService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void add(EventRequestDTO event) {

		logger.warn("Queued Event : {}", event.getEventId());

		queue.offer(event);
	}

	@Async("eventExecutor")
	@Scheduled(fixedDelay = 30000)
	public void retryQueue() {

		while (!queue.isEmpty()) {

			EventRequestDTO event = queue.peek();

			try {

				restTemplate.postForEntity(
						accountServiceUrl + "/api/accounts/" + event.getAccountId() + "/transactions", event,
						Void.class);

				logger.info("Retry Successful : {}", event.getEventId());

				queue.poll();

			} catch (Exception ex) {

				logger.warn("Retry Failed : {}", event.getEventId());

				break;
			}

		}

	}

}