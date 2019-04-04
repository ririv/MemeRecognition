package com.riri.emojirecognition.exception;

public final class RoleIsNotPresentException extends RuntimeException {

    public RoleIsNotPresentException(final String message) {
        super(message);
    }

    public RoleIsNotPresentException() {
        super("The role is not present");
    }

}
