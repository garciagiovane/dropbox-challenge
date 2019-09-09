package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<UserFile> getAllFiles();

    List<UserFile> getAllFilesFromUserByID(String id) throws Exception;
    UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException;
    ResponseEntity deleteFileById(String userId, String fileId) throws UserNotFoundException;
}
