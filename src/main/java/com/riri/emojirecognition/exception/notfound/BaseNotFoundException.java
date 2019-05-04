package com.riri.emojirecognition.exception.notfound;

public class BaseNotFoundException extends RuntimeException {

    public BaseNotFoundException(final String message) {
        super(message);
    }

    public BaseNotFoundException(final Long id) {
        super("Not found, id: " + id);
    }
}
