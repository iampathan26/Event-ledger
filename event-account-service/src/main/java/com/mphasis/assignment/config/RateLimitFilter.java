package com.mphasis.assignment.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private final Bucket bucket;

	public RateLimitFilter(Bucket bucket) {
		this.bucket = bucket;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (bucket.tryConsume(1)) {

			filterChain.doFilter(request, response);

		} else {

			//response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.setContentType("application/json");

			response.getWriter().write("""
					{
					  "success": false,
					  "message": "Too Many Requests. Please try again after some time."
					}
					""");
		}
	}
}