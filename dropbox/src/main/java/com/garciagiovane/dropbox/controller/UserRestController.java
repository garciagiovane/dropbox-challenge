package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.UserService;
import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.*;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import io.swagger.annotations.*;
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
    @ApiResponse(code = 200, message = "return a list of all users in the database")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @ApiOperation(value = "Receive a JSON with name and email fields to save it on the database and return the created object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return an object representing the user saved"),
            @ApiResponse(code = 500, message = "exception occurred, user already registered, email is already in use")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User createUser(@ApiParam(value = "JSON with name and email") @RequestBody UserDTO userDTO) throws UserAlreadyRegisteredException {
        return userService.createUser(userDTO);
    }

    @ApiOperation(value = "Return all information from a user according the id passed or throw an exception for user not found")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return a user of the database"),
            @ApiResponse(code = 404, message = "exception occurred, user was not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getUserById(@ApiParam(value = "user id", required = true) @PathVariable String id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @ApiOperation(value = "Receive a JSON with name and email fields and update a user saved or throw an exception for user not found")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return the user updated"),
            @ApiResponse(code = 404, message = "exception occurred, user was not found"),
            @ApiResponse(code = 500, message = "exception occurred, user is already registered, email changed is already in use"),
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User updateUserById(@ApiParam(value = "JSON with user name and email") @RequestBody UserDTO userDTO, @ApiParam(value = "user id", required = true) @PathVariable String id) throws UserNotFoundException, UserAlreadyRegisteredException {
        return userService.updateUserById(userDTO, id);
    }

    @ApiOperation(value = "Delete a user according the id passed or throw an exception for user not found")
    @ApiResponses( value = {
            @ApiResponse(code = 204, message = "no message returned"),
            @ApiResponse(code = 404, message = "exception occurred, user was not found"),
            @ApiResponse(code = 500, message = "exception occurred, access to ftp was denied or user directory in the ftp server doesn't exists")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@ApiParam(value = "user id", required = true) @PathVariable String id) throws UserNotFoundException, ConnectionRefusedException, NoFilesFoundException, DirectoryNotFoundException, IOException {
        return userService.deleteUserById(id);
    }

    @ApiOperation(value = "Receive a JSON with file id (previously saved) and a user id (also previously saved), then share the file with this user or throw an exception for user not found or file not found")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "no message returned"),
            @ApiResponse(code = 404, message = "exception occurred, user was not found or no files was not found")
    })
    @PostMapping("/{ownerId}")
    public ResponseEntity createShare(@ApiParam(value = "owner's file id", required = true) @PathVariable String ownerId, @ApiParam(value = "JSON with file id and user id that will receive the file shared", required = true) @RequestBody ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException {
        return userService.shareFileById(ownerId, shareEntity);
    }
}