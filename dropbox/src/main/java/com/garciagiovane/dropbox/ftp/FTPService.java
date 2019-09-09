package com.garciagiovane.dropbox.ftp;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FTPService {
    boolean saveFile(MultipartFile fileToSave) throws IOException;
    boolean deleteFile(String fileName) throws IOException;
}
