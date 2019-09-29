package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPErrorExitingException extends RuntimeException {
    public FTPErrorExitingException() {
        super("Error while logging out");
    }

    public FTPErrorExitingException(String message) {
        super(message);
    }
}
