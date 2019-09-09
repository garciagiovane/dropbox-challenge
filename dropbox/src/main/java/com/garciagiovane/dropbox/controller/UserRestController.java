package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.UserService;
import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}