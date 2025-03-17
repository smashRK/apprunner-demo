package com.turtle.portal.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.turtle.portal.customer"})
public class CustomerPortal {

	public static void main(String[] args) {
		SpringApplication.run(CustomerPortal.class, args);
	}

}