package com.khanenka.restapiservlet;


import com.khanenka.restapiservlet.util.DBConnection;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;


public class DBConnectionTest {

    private Connection connection;

    @Before
    public void setUp() {
        connection = DBConnection.getConnection();
    }

    @Test
    public void getConnection() {
        assertNotNull("Connection should not be null", connection);
    }


    @Test
    public void testSingletonBehaviour() throws Exception {
        Connection connection1 = DBConnection.getConnection();
        Connection connection2 = DBConnection.getConnection();
        assertSame("Both connections should be the same instance", connection1, connection2);
        connection1.close();
        connection2.close(); // Закрываем соединения
    }



}