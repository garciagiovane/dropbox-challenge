package com.garciagiovane.dropbox.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String _id;
    private String name;
    private String email;
    private List<UserFile> files = new ArrayList<>();

    public String get_id(){
        return this._id;
    }
}
