package com.khanenka.restapiservlet.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class StudentServlet  extends HttpServlet{
    Properties properties = new Properties();
    public static void main(String args[]) throws IOException {


        // properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("C:/Users/leonid/IdeaProjects/RestApiServlet/src/main/resources/db.properties"));


//    public void init(ServletConfig servletConfig) {
//        try {
//
//            properties.load(Files.newInputStream(Paths.get("")));
//            super.init(servletConfig);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        try {
//            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "1234");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//
//            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "test", "1234");
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Подготовка SQL запроса
//        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
//        try {
//            stmt = connection.prepareStatement(sql);
//
//            stmt.setString(1, "username");
//            stmt.setString(2, "email");
//            int rowsAffected = stmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        // Выполнение запроса на добавление данных в базу
//
//
    }
//
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String propertiesFilePath = context.getRealPath("/WEB-INF/resources.db.properties");
        String dbUrl = properties.getProperty("dbUrl");
        String dbUsername = properties.getProperty("dbUsername");
        String dbPassword = properties.getProperty("dbPassword");
        String dbDriver = properties.getProperty("dbDriver");
        try {
            Class.forName(properties.getProperty("dbDriver"));
            Connection c = DriverManager
                    .getConnection(dbUrl,
                            dbUsername, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }


        // Connect to the database


    }
    // использование полученных значений
    // например, подключение к базе данных


