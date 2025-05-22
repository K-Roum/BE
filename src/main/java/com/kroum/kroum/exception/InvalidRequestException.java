package com.kroum.kroum.exception;

// 400 에러 익셉션
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException() {
        super("Invalid request");
    }
}
