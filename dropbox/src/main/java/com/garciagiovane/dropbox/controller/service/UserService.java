package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserService {
    User createUser(UserDTO userDTO);
    User getUserById(String id) throws UserNotFoundException;
    User updateUserById(UserDTO userDTO, String id) throws UserNotFoundException;
    ResponseEntity deleteUserById(String id) throws UserNotFoundException, NoFilesFoundException, DirectoryNotFoundException, IOException, ConnectionRefusedException;
    ResponseEntity shareFileById(String ownerId, ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException;
    Page<User> findAll(Pageable pageable);
}
