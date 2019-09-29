package com.garciagiovane.dropbox.contract.v1.user.model.response;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private List<FileModel> files;
}
