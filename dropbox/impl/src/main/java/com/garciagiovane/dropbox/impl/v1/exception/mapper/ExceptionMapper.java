package com.garciagiovane.dropbox.impl.v1.exception.mapper;

import com.garciagiovane.dropbox.impl.v1.exception.Issue;

public class ExceptionMapper {
    public static Issue mapToIssue(Exception exception) {
        return Issue.builder()
                .message(exception.getMessage())
                .build();
    }
}
