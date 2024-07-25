package com.khanenka.restapiservlet.exception;

/**
 * public Класс DatabaseConnectionException
 * Исключение, возникающее при проблемах с подключением к базе данных.
 *
 * @author Khanenka
 * *
 * * @version 1.0
 */
public class DatabaseConnectionException extends RuntimeException {
    /**
     * @param message Сообщение, описывающее причину исключения.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
