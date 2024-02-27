package com.recipe.recipebook.exception;

public class OutOfPageRangeException extends RuntimeException{
    public OutOfPageRangeException(String message) {
        super(message);
    }
}
