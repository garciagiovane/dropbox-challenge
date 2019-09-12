package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Page<UserFile> getAllFiles(Pageable pageable);
    UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException;
    ResponseEntity deleteFileById(String userId, String fileId) throws UserNotFoundException;
    UserFile updateFile(UserFile userFile);

    Page<UserFile> getFilesByName(String userId, String fileName, Pageable pageable) throws UserNotFoundException, NoFilesFoundException;
    Page<UserFile> getAllFilesFromUserByID(String idOwner, Pageable pageable) throws NoFilesFoundException;
}
