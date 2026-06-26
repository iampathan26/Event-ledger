package com.mphasis.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EventAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventAccountServiceApplication.class, args);
	}

}
