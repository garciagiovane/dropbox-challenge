package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.UserService;
import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping("")
    public User createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUserById(@RequestBody UserDTO userDTO, @PathVariable String id) throws UserNotFoundException {
        return userService.updateUserById(userDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable String id) throws UserNotFoundException {
        return userService.deleteUserById(id);
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity createShare(@PathVariable String ownerId, @RequestBody ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException {
        return userService.shareFileById(ownerId, shareEntity);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}