package com.khanenka.restapiservlet.util;


import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * public Класс DBConnection
 *
 * @author Khanenka
 * *
 * * @version 1.0
 */
public class DBConnection {
    private static Connection connection = null;
    private static PostgreSQLContainer<?> postgreSQLContainer;

    /**
     * приватный конструктор
     */
    private DBConnection() {
    }

    /**
     * метод singleton - создание подключения
     *
     * @return connection
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = new Properties();
                try (InputStream input =
                             DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input == null) {
                        return null;
                    }
                    properties.load(input);
                }
                String url = properties.getProperty("dbUrl");
                String user = properties.getProperty("dbUsername");
                String password = properties.getProperty("dbPassword");
                String driver = properties.getProperty("dbDriver");
                String version =properties.getProperty("dbVersion");
                // Используем Testcontainers
                postgreSQLContainer = new PostgreSQLContainer<>(version)
                        .withDatabaseName(url)
                        .withUsername(user)
                        .withPassword(password);
                postgreSQLContainer.start();
                String urlContainer = postgreSQLContainer.getJdbcUrl();
                String userContainer = postgreSQLContainer.getUsername();
                String passwordContainer = postgreSQLContainer.getPassword();
                Class.forName(driver);
                connection = DriverManager.getConnection(urlContainer, userContainer, passwordContainer);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
