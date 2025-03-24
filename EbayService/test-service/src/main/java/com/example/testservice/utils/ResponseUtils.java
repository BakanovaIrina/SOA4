package com.example.testservice.utils;

import com.example.testservice.exception.AppRuntimeException;
import com.example.testservice.soap.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ResponseUtils {

    public ResponseEntity<ErrorResponse> buildResponseWithMessage(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message(message)
                        .code(status.value())
                        .time(Instant.now().toString())
                        .build());
    }

    public ResponseEntity<ErrorResponse> buildResponseByException(AppRuntimeException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .code(exception.getStatus().value())
                        .time(Instant.now().toString())
                        .build());
    }
}

