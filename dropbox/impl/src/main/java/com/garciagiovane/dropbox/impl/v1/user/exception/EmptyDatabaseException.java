package com.garciagiovane.dropbox.impl.v1.user.exception;

public class EmptyDatabaseException extends RuntimeException {
    public EmptyDatabaseException (){
        super("Database is empty");
    }

    public EmptyDatabaseException (String message){
        super(message);
    }
}
