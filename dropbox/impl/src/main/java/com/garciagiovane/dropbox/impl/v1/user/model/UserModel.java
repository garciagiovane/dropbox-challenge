package com.garciagiovane.dropbox.impl.v1.user.model;

import com.garciagiovane.dropbox.impl.v1.file.model.UserFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class UserModel {
    private String id;
    private String name;
    private String email;
    private List<UserFile> files;
}
