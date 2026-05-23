package com.eaglebank.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private List<ValidationError> details;

    @Getter
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
        private String type;
    }
}
