package com.Turtle.poccurd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.Turtle.poccurd.repository")  // Ensure correct package
@EntityScan("com.Turtle.poccurd.model") // Ensure it scans entities
public class PoccurdApplication {
	public static void main(String[] args) {
		SpringApplication.run(PoccurdApplication.class, args);
	}
}