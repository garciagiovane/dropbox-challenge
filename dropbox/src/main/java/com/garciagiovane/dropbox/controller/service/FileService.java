package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    Page<UserFile> getAllFiles(Pageable pageable);
    UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException, IOException, ConnectionRefusedException;
    ResponseEntity deleteFileById(String userId, String fileId) throws UserNotFoundException, NoFilesFoundException, IOException, ConnectionRefusedException, DirectoryNotFoundException;
    UserFile updateFile(UserFile userFile);
    Page<UserFile> getFilesByName(String userId, String fileName, Pageable pageable) throws UserNotFoundException, NoFilesFoundException;
    Page<String> getAllFilesFromUserByID(String idOwner, Pageable pageable) throws NoFilesFoundException, DirectoryNotFoundException, IOException, ConnectionRefusedException;
    boolean deleteDirectory(String userId) throws ConnectionRefusedException, IOException;
    void deleteDatabaseFiles(List<UserFile> files);
}
