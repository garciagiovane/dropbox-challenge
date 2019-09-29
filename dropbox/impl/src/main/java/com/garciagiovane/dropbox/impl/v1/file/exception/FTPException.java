package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPException extends RuntimeException {
    public FTPException() {
        super("FTP error");
    }

    public FTPException(String message) {
        super(message);
    }
}
