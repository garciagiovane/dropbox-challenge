package com.garciagiovane.dropbox.impl.v1.file.stubs;

import com.garciagiovane.dropbox.impl.v1.file.repository.FileEntity;

import java.util.ArrayList;

public class FileStub {
    public static FileEntity getFileEntity() {
        return FileEntity.builder()
                .id("fileId")
                .idOwner("ownerId")
                .originalName("test.txt")
                .viewers(new ArrayList<>())
                .build();
    }
}
