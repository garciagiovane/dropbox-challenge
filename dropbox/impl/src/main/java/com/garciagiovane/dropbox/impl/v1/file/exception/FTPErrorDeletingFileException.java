package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPErrorDeletingFileException extends RuntimeException {
    public FTPErrorDeletingFileException() {
        super("Error deleting file");
    }

    public FTPErrorDeletingFileException(String message) {
        super(message);
    }
}
