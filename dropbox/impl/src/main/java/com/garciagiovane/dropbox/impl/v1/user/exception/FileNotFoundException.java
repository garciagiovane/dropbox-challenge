package com.garciagiovane.dropbox.impl.v1.user.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File not found");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
