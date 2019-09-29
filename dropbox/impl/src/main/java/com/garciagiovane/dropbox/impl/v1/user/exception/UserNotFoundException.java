package com.garciagiovane.dropbox.impl.v1.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(){
        super("User not found");
    }

    public UserNotFoundException(String message){
        super(message);
    }
}
