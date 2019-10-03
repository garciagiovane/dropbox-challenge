package com.garciagiovane.dropbox.impl.v1.exception;

import com.garciagiovane.dropbox.impl.v1.exception.mapper.ExceptionMapper;
import com.garciagiovane.dropbox.impl.v1.user.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerApplication {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException e) {
        log.error(e.getExceptions().toString());
        ExceptionModel exceptionModel = ExceptionModel.builder()
                .status(e.getStatus().value())
                .errors(e.getExceptions().stream().map(ExceptionMapper::mapToIssue).collect(Collectors.toList()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(e.getStatus()).body(exceptionModel);
    }

    private ResponseEntity error(HttpStatus status, Exception e) {
        log.error(e.getMessage());
        ExceptionModel exception = ExceptionModel.builder()
                .status(status.value())
                .errors(List.of(ExceptionMapper.mapToIssue(e)))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(exception);
    }
}
