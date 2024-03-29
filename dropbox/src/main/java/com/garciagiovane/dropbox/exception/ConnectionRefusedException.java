package com.garciagiovane.dropbox.exception;

public class ConnectionRefusedException extends Exception {
    public ConnectionRefusedException() {
        super("FTP connection refused");
    }

    public ConnectionRefusedException(String message) {
        super(message);
    }
}
