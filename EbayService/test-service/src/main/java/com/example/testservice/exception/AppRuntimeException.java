package com.example.testservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppRuntimeException extends RuntimeException {
    private final HttpStatus status;

    public AppRuntimeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
