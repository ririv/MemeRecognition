package com.riri.emojirecognition.exception.notfound;

public class ModelNotFoundException extends BaseNotFoundException {

    public ModelNotFoundException(final String message) {
        super(message);
    }

    public ModelNotFoundException() {
        super("The model is not found");
    }

    public ModelNotFoundException(final Long id) {
        super(id);
    }

}
