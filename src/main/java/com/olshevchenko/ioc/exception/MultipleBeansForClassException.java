package com.olshevchenko.ioc.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class MultipleBeansForClassException extends RuntimeException {
    public MultipleBeansForClassException(String message) {
        super(message);
    }
}
