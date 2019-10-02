package com.garciagiovane.dropbox.stubs;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;

import java.util.ArrayList;
import java.util.List;

public class FileStub {
    public static FileModel getFileModel() {
        return FileModel.builder()
                .id("fileId")
                .idOwner("someId")
                .originalName("test.txt")
                .ftpName("fileId-test.txt")
                .viewers(new ArrayList<>()).build();
    }

    public static List<FileModel> getListOfFiles() {
        List<FileModel> files = new ArrayList<>();
        files.add(FileStub.getFileModel());
        return files;
    }
}
