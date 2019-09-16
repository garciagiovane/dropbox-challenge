package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.UserService;
import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(value = "Dropbox challenge")
@RestController
@RequestMapping(path = "/users")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users from the database, you can define page and size variable in the URI to paginate")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @ApiOperation(value = "Receive a JSON with name and email fields to save it on the database and return the created object")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @ApiOperation(value = "Return all information from a user according the id passed or throw an exception for user not found")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getUserById(@PathVariable String id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @ApiOperation(value = "Receive a JSON with name and email fields and update a user saved or throw an exception for user not found")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User updateUserById(@RequestBody UserDTO userDTO, @PathVariable String id) throws UserNotFoundException {
        return userService.updateUserById(userDTO, id);
    }

    @ApiOperation(value = "Delete a user according the id passed or throw an exception for user not found")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable String id) throws UserNotFoundException, ConnectionRefusedException, NoFilesFoundException, DirectoryNotFoundException, IOException {
        return userService.deleteUserById(id);
    }

    @ApiOperation(value = "Receive a JSON with file id (previously saved) and a user id (also previously saved), then share the file with this user or throw an exception for user not found or file not found")
    @PostMapping("/{ownerId}")
    public ResponseEntity createShare(@PathVariable String ownerId, @RequestBody ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException {
        return userService.shareFileById(ownerId, shareEntity);
    }
}