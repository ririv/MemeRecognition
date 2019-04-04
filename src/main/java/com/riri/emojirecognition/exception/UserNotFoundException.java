package com.riri.emojirecognition.exception;

public final class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("The user not found");
    }

}
