package com.recipe.recipebook.exception;

public class OpenAIAPIKeyIncorrectException extends RuntimeException{
    public OpenAIAPIKeyIncorrectException(String message) {
        super(message);
    }
}
