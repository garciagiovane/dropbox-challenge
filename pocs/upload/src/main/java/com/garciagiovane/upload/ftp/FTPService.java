package com.garciagiovane.upload.ftp;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FTPService {
    boolean saveFile(MultipartFile fileToSave) throws IOException;
    InputStream searchFileByName(String fileName) throws IOException;
    boolean deleteFile(String fileName) throws IOException;
    List<String> getAllFiles() throws IOException;
}
