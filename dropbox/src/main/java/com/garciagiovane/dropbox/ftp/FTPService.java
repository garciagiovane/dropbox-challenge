package com.garciagiovane.dropbox.ftp;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FTPService {
    boolean saveFile(MultipartFile fileToSave) throws IOException, ConnectionRefusedException;
    boolean deleteFile(String fileName) throws IOException, ConnectionRefusedException;
    boolean renameFile(String originalFileName, String newFileName) throws IOException, ConnectionRefusedException;
}
