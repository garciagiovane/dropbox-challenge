package com.garciagiovane.dropbox.exception;

public class UserAlreadyRegisteredException extends Exception {
    public UserAlreadyRegisteredException() {
        super("User already registered");
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
