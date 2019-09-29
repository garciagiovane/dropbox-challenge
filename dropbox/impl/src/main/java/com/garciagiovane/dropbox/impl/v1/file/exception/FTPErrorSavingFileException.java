package com.garciagiovane.dropbox.impl.v1.file.exception;

public class FTPErrorSavingFileException extends RuntimeException {
    public FTPErrorSavingFileException() {
        super("Error saving file");
    }

    public FTPErrorSavingFileException(String message) {
        super(message);
    }
}
