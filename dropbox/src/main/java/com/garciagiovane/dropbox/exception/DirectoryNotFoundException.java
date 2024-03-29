package com.garciagiovane.dropbox.exception;

public class DirectoryNotFoundException extends Exception {
    public DirectoryNotFoundException() {
        super("Directory not found");
    }

    public DirectoryNotFoundException(String message) {
        super(message);
    }
}
