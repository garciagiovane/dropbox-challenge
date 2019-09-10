package com.garciagiovane.dropbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserFile {
    private String id;
    private String originalName;
    private String ftpName;
    private String idOwner;
    private List<User> viewers;
}
