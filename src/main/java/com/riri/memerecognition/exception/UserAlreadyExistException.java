package com.riri.memerecognition.exception;

public final class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException() {
        super("The user already exists");
    }

}
