package com.garciagiovane.dropbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity handleUserNotFoundException(UserNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({NoFilesFoundException.class})
    public ResponseEntity handleNoFilesFoundException(NoFilesFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({DirectoryNotFoundException.class})
    public ResponseEntity handleDirectoryNotFoundException(DirectoryNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(ConnectionRefusedException.class)
    public ResponseEntity handleConnectionRefusedException(ConnectionRefusedException e){
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity handleUserAlreadyRegisteredException(UserAlreadyRegisteredException e){
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity error(HttpStatus status, Exception e){
        log.error("Exception: " + e);
        ExceptionModel exceptionModel = ExceptionModel.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .debugMessage(e.getLocalizedMessage())
                .build();

        return ResponseEntity.status(status).body(exceptionModel);
    }
}