package com.garciagiovane.dropbox.impl.v1.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Viewer {
    private String id;
    private String name;
    private String email;
}
