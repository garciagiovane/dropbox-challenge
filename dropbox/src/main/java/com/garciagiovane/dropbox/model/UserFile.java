package com.garciagiovane.dropbox.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
public class UserFile {
    private String id;
    private String completeName;
    private String idOwner;
    private List<User> viewers;
}
