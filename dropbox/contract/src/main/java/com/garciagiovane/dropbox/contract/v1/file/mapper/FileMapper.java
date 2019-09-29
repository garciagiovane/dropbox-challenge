package com.garciagiovane.dropbox.contract.v1.file.mapper;

import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;

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
}
