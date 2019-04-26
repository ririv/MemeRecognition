package com.riri.emojirecognition.exception;

public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(final String message) {
        super(message);
    }

    public ModelNotFoundException() {
        super("The model is not found");
    }

}
