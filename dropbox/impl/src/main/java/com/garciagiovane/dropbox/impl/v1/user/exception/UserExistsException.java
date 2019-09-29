package com.garciagiovane.dropbox.impl.v1.user.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(){
        super("User already registered");
    }

    public UserExistsException(String message){
        super(message);
    }
}
