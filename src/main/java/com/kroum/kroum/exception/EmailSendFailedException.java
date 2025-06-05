package com.kroum.kroum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailSendFailedException extends RuntimeException {

    public EmailSendFailedException(String message) {
        super(message);
    }
}
