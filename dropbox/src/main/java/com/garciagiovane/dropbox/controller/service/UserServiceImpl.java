package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDTO userDTO){
        return userRepository.save(userDTO.user());
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUserById(UserDTO userDTO, String id) throws UserNotFoundException {
        return userRepository.findById(id).map(userFound -> {
            User user = userDTO.user();
            user.setId(id);
            user.setFiles(userFound.getFiles());
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public ResponseEntity deleteUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
