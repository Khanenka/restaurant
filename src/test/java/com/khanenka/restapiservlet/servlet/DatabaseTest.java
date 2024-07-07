package com.khanenka.restapiservlet.servlet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DatabaseTest {

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer("postgres:13-alpine")
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("test");

    @Test
    void testDatabaseConnection() {
        String jdbcUrl = postgresqlContainer.getJdbcUrl();
        String username = postgresqlContainer.getUsername();
        String password = postgresqlContainer.getPassword();

        // Ваш код для проверки соединения с базой данных
    }
}