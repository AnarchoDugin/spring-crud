package com.zakuwarrior.crudhttp;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@SpringBootApplication
public class CrudHttpApplication {
	private final DataSource dataSource;

	public CrudHttpApplication(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(CrudHttpApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void testDatabaseConnection() {
		try (Connection connection = dataSource.getConnection()) {
			log.info("Connection to PostgreSQL established!");
			log.info("URL: {}", connection.getMetaData().getURL());
			log.info("Username: {}", connection.getMetaData().getUserName());
		} catch (SQLException e) {
			log.error("Failed to connect to PostgreSQL", e);
		}
	}
}
