package ru.t1.java.demo.exception;

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
