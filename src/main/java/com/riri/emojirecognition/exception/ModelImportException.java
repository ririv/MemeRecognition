package com.riri.emojirecognition.exception;

public class ModelImportException extends RuntimeException{
    public ModelImportException(final String message){
        super(message);
    }

    public ModelImportException(){
        super("Fail to import the model");
    }

    public ModelImportException(final Long id){
        super("Fail to import the model, id:"+id);
    }
}
