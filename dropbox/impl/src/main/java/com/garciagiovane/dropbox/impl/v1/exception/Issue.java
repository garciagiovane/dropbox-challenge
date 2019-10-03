package com.garciagiovane.dropbox.impl.v1.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
    private String message;
}
