package com.riri.memerecognition.exception.notfound;

public class ImgNotFoundException extends BaseNotFoundException {

        public ImgNotFoundException(final String message) {
            super(message);
        }

        public ImgNotFoundException() {
            super("The img is not found");
        }

        public ImgNotFoundException(final Long id) {
            super(id);
        }
    }
