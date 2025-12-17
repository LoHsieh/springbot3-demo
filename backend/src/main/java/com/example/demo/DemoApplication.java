package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		String dbHost = System.getenv("DB_HOST");
		logger.info("Check Env: DB_HOST = " + (dbHost != null ? dbHost : "NOT SET"));
		SpringApplication.run(DemoApplication.class, args);
	}

}
