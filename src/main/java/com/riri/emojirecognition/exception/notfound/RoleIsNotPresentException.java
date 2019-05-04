package com.riri.emojirecognition.exception.notfound;

public final class RoleIsNotPresentException extends BaseNotFoundException {

    public RoleIsNotPresentException(final String message) {
        super(message);
    }

    public RoleIsNotPresentException() {
        super("The role is not present");
    }

    public RoleIsNotPresentException(final Long id){
        super(id);
    }

}
