package com.olshevchenko.ioc.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class SourceParseException extends RuntimeException {
    public SourceParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
