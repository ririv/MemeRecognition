package com.riri.memerecognition.exception.notfound;

public final class UserNotFoundException extends BaseNotFoundException {

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("The user is not found");
    }

    public UserNotFoundException(final Long id) {
        super(id);
    }
}
