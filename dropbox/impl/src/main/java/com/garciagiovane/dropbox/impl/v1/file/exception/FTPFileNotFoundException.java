package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPFileNotFoundException extends RuntimeException {
    public FTPFileNotFoundException() {
        super("File not found");
    }

    public FTPFileNotFoundException(String message) {
        super(message);
    }
}
