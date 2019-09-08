package com.garciagiovane.dropbox.model;

import lombok.Data;

import java.util.List;

@Data
public class UserFile {
    private Object _id;
    private String completeName;
    private User owner;
    private List<User> viewers;
}
