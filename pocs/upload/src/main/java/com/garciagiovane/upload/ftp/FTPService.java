package com.garciagiovane.upload.ftp;

import com.garciagiovane.upload.exceptions.CantSaveFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FTPService {
    void connect(String host);
    void login(String userName, String password);
    boolean saveFile(MultipartFile fileToSave) throws CantSaveFileException, IOException;
    InputStream searchFileByName(String fileName) throws IOException;
}
