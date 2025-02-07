package ru.t1.java.service_1.exception;

public class DataSourceErrorException extends RuntimeException {
    public DataSourceErrorException() {
    }

    public DataSourceErrorException(String message) {
        super(message);
    }

    public DataSourceErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
