package com.olshevchenko.ioc.exception;

/**
 * @author Oleksandr Shevchenko
 */
public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
    public BeanInstantiationException(String message) {
        super(message);
    }
}
