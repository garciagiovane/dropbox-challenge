package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserRestController {
    private UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PutMapping("/{id}")
    public User updateUserById(@RequestBody User user, @PathVariable String id) throws UserNotFoundException {
        return userRepository.findById(id).map(userFound -> {
            user.setId(id);
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable String id) throws UserNotFoundException {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseThrow(UserNotFoundException::new);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}