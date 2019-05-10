package com.riri.emojirecognition.exception;

public final class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException() {
        super("The user already exists");
    }

}
