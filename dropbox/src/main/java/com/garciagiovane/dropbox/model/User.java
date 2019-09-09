package com.garciagiovane.dropbox.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private String id;
    private String name;
    private String email;
    private List<UserFile> files = new ArrayList<>();
}
