package com.garciagiovane.dropbox.ftp;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FTPService {
    boolean saveFile(MultipartFile fileToSave, String userId) throws IOException, ConnectionRefusedException;
    boolean deleteFile(String fileName, String userId) throws IOException, ConnectionRefusedException, DirectoryNotFoundException;
    boolean renameFile(String originalFileName, String newFileName, String ownerId) throws IOException, ConnectionRefusedException;
    Page<String> getAllFilesByUserId(String userId) throws IOException, ConnectionRefusedException, DirectoryNotFoundException;
    boolean directoryExists(String userId) throws ConnectionRefusedException, IOException;
}
