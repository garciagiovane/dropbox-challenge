package com.garciagiovane.dropbox.ftp;

import com.garciagiovane.dropbox.dto.UserFileDTO;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FTPService {
    boolean saveFile(MultipartFile fileToSave, String userId) throws IOException, ConnectionRefusedException;
    boolean deleteFile(String fileName, String userId) throws IOException, ConnectionRefusedException, DirectoryNotFoundException;
    boolean renameFile(String originalFileName, String newFileName, String ownerId) throws IOException, ConnectionRefusedException;
    List<UserFileDTO> getAllFilesByUserId(String userId) throws IOException, ConnectionRefusedException, DirectoryNotFoundException;
    boolean directoryExists(String userId) throws ConnectionRefusedException, IOException;
    boolean deleteDirectory(String userId) throws ConnectionRefusedException, IOException;
}
