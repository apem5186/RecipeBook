package com.recipe.recipebook.exception;

public class ChatGPTModelDeprecatedException extends RuntimeException{
    public ChatGPTModelDeprecatedException(String message) {
        super(message);
    }
}
