package com.kroum.kroum.exception;

// 500 에러 익셉션
public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException() {
        super("Internal Server Error");
    }
}