package com.garciagiovane.dropbox.impl.v1.file.mapper;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.repository.FileEntity;

public class FileMapper {
    public static FileEntity mapToEntity(FileModel fileModel){
        return FileEntity.builder()
                .id(fileModel.getId())
                .idOwner(fileModel.getIdOwner())
                .originalName(fileModel.getOriginalName())
                .ftpName(fileModel.getFtpName())
                .viewers(fileModel.getViewers())
                .build();
    }

    public static FileModel mapToFileModel(FileEntity fileEntity){
        return FileModel.builder()
                .id(fileEntity.getId())
                .idOwner(fileEntity.getIdOwner())
                .originalName(fileEntity.getOriginalName())
                .ftpName(fileEntity.getFtpName())
                .viewers(fileEntity.getViewers())
                .build();
    }
}
