package com.riri.emojirecognition.exception;

public class ImgNotFoundException extends RuntimeException {

        public ImgNotFoundException(final String message) {
            super(message);
        }

        public ImgNotFoundException() {
            super("The img is not found");
        }

    }
