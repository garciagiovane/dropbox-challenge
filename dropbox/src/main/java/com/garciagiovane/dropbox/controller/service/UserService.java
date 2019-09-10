package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);
    User getUserById(String id) throws UserNotFoundException;
    User updateUserById(UserDTO userDTO, String id) throws UserNotFoundException;
    ResponseEntity deleteUserById(String id) throws UserNotFoundException;
    ResponseEntity shareFileById(String ownerId, ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException;

    default List<User> getAllUsers() {
        return Collections.emptyList();
    }
}
