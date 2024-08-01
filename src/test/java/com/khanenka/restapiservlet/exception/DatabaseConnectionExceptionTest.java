package com.khanenka.restapiservlet.exception;

import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DatabaseConnectionExceptionTest {

    @Test
    public void DatabaseConnectionException() {
        String expectedMessage = "Ошибка подключения к базе данных";
        DatabaseConnectionException exception = new DatabaseConnectionException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage());
    }

}