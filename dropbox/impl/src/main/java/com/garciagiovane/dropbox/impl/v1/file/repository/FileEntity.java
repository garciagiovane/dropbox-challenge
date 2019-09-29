package com.garciagiovane.dropbox.impl.v1.file.repository;

import com.garciagiovane.dropbox.impl.v1.user.model.Viewer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity {
    private String id;
    private String originalName;
    private String ftpName;
    private String idOwner;
    private List<Viewer> viewers;
}
