package me.dio.service.exception;

public class ViolateConstraintsException extends RuntimeException{
    public ViolateConstraintsException(String message) {
        super(message);
    }

    public ViolateConstraintsException(String message, Throwable cause) {
        super(message, cause);
    }
}
