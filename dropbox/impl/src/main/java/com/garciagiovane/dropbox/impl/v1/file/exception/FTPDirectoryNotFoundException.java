package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPDirectoryNotFoundException extends RuntimeException {
    public FTPDirectoryNotFoundException() {
        super("Directory not found");
    }

    public FTPDirectoryNotFoundException(String message) {
        super(message);
    }
}
