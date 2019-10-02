package com.garciagiovane.dropbox.impl.v1.exception;

import com.garciagiovane.dropbox.impl.v1.file.exception.*;
import com.garciagiovane.dropbox.impl.v1.user.exception.EmptyDatabaseException;
import com.garciagiovane.dropbox.impl.v1.user.exception.FileNotFoundException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserExistsException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerApplication {

    @ExceptionHandler(EmptyDatabaseException.class)
    public ResponseEntity handleEmptyDatabaseException(EmptyDatabaseException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleFileNotFoundException(FileNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity handleUserExistsException(UserExistsException e) {
        return error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(FTPDirectoryNotFoundException.class)
    public ResponseEntity handleFTPDirectoryNotFoundException(FTPDirectoryNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(FTPErrorDeletingFileException.class)
    public ResponseEntity handleFTPErrorDeletingFileException(FTPErrorDeletingFileException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(FTPErrorExitingException.class)
    public ResponseEntity handleFTPErrorExitingException(FTPErrorExitingException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(FTPErrorSavingFileException.class)
    public ResponseEntity handleFTPErrorSavingFileException(FTPErrorSavingFileException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(FTPException.class)
    public ResponseEntity handleFTPException(FTPException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(FTPFileNotFoundException.class)
    public ResponseEntity handleFTPFileNotFoundException(FTPFileNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleFTPFileNotFoundException(Exception e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity error(HttpStatus status, Exception e) {
        log.error(e.getMessage());
        ExceptionModel exception = ExceptionModel.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .debugMessage(e.getLocalizedMessage())
                .build();
        return ResponseEntity.status(status).body(exception);
    }

}
