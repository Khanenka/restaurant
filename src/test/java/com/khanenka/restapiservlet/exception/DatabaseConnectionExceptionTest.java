package com.khanenka.restapiservlet.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DatabaseConnectionExceptionTest {

    @Test
    public void DatabaseConnectionException() {
        String expectedMessage = "Ошибка подключения к базе данных";

        // Создаем экземпляр исключения
        DatabaseConnectionException exception = new DatabaseConnectionException(expectedMessage);

        // Проверяем, что сообщение исключения соответствует ожидаемому
        assertEquals(expectedMessage, exception.getMessage());
    }

}