package com.garciagiovane.dropbox.impl.v1.file.mapper;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.file.repository.FileEntity;
import org.apache.commons.net.ftp.FTPFile;

public class FileMapper {
    public static FileEntity mapToEntity(FileModel fileModel) {
        return FileEntity.builder()
                .id(fileModel.getId())
                .idOwner(fileModel.getIdOwner())
                .originalName(fileModel.getOriginalName())
                .ftpName(fileModel.getFtpName())
                .viewers(fileModel.getViewers())
                .build();
    }

    public static FileModel mapToFileModel(FileEntity fileEntity) {
        return FileModel.builder()
                .id(fileEntity.getId())
                .idOwner(fileEntity.getIdOwner())
                .originalName(fileEntity.getOriginalName())
                .ftpName(fileEntity.getFtpName())
                .viewers(fileEntity.getViewers())
                .build();
    }

    public static ImplFTPFile mapToFTPFile(FTPFile ftpFile) {
        return ImplFTPFile.builder()
                .name(ftpFile.getName())
                .size(ftpFile.getSize())
                .build();
    }
}
