package com.recipe.recipebook.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NetworkErrorException extends RuntimeException{

    @Getter
    private final HttpStatus status;
    private final String message;

    public NetworkErrorException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
