package com.soa.utils;

import com.soa.model.ErrorResponse;
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
}

