package com.khanenka.restapiservlet.util;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;

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
    /**
     * приватный конструктор
     */
    private DBConnection() {
    }
    /**
     * метод ingleton - созданиеподключения
     *
     * @return connection
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Загружаем настройки из файла properties
                Properties properties = new Properties();
                try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                    if (input == null) {
                        return null;
                    }
                    // Загружаем свойства
                    properties.load(input);
                }
                // Получаем параметры подключения
                String url = properties.getProperty("dbUrl");
                String user = properties.getProperty("dbUsername");
                String password = properties.getProperty("dbPassword");
                String driver = properties.getProperty("dbDriver");
                // Регистрация драйвера и создание соединения
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | IOException ex) {
                ex.printStackTrace();
            } catch (SQLException e) {
                throw new DatabaseConnectionException(e.getMessage());
            }
        }
        return connection;
    }
}
