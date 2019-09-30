package com.garciagiovane.dropbox.contract.v1.file.mapper;

import com.garciagiovane.dropbox.contract.v1.file.model.response.FTPFileResponse;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;

public class FileMapper {
    public static FileResponse mapToContract(FileModel model) {
        return FileResponse.builder()
                .id(model.getId())
                .idOwner(model.getIdOwner())
                .originalName(model.getOriginalName())
                .ftpName(model.getFtpName())
                .viewers(model.getViewers())
                .build();
    }

    public static FTPFileResponse mapToFTPFileResponse(ImplFTPFile implFTPFile) {
        return FTPFileResponse.builder()
                .name(implFTPFile.getName())
                .size(implFTPFile.getSize())
                .build();
    }
}
