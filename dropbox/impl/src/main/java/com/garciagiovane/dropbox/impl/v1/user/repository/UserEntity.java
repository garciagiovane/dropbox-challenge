package com.garciagiovane.dropbox.impl.v1.user.repository;

import com.garciagiovane.dropbox.impl.v1.file.model.UserFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEntity {
    private String id;
    private String name;
    private String email;
    private List<UserFile> files;
}
