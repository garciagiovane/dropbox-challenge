package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;

import java.util.List;

public interface FileService {
    List<UserFile> getAllFilesFromUserByID(String id) throws Exception;
    UserFile saveFile(String userId, UserFile userFile) throws UserNotFoundException;
}
