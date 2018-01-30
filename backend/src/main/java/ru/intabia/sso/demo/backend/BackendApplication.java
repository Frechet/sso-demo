package ru.intabia.sso.demo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Main class for spring boot starter.
 */
@SpringBootApplication
public class BackendApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BackendApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
