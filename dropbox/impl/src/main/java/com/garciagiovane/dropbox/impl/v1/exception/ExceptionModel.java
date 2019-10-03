package com.garciagiovane.dropbox.impl.v1.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionModel {
    private int status;
    private LocalDateTime timestamp;
    private List<Issue> errors;
}
