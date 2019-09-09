package com.garciagiovane.dropbox.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class UserFile {
    private String id;
    private String completeName;
    private User owner;
    private List<User> viewers;
}
