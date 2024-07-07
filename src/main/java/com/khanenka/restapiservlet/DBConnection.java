package com.khanenka.restapiservlet;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "admin", "1234");
            System.out.println("Connected to the database 1");
        } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;

        }


    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection close");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}



