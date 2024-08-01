package com.khanenka.restapiservlet.exceptions;


/**
 * <p>DatabaseConnectionException class.</p>
 *
 * @author leonid
 * @version $Id: $Id
 */
public class DatabaseConnectionException extends RuntimeException {
    /**
     * <p>Constructor for DatabaseConnectionException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }
}
