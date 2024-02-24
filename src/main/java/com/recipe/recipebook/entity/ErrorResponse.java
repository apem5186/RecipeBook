package com.recipe.recipebook.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private HttpStatus status;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ResponseEntity<ErrorResponse> toResponseEntity() {
        return new ResponseEntity<>(this, this.status);
    }
}
