package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.repository.FileRepository;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class FileRestController {
    private FileRepository fileRepository;
    private UserRepository userRepository;

    @Autowired
    public FileRestController(FileRepository fileRepository, UserRepository userRepository){
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}/files")
    public List<UserFile> getAllFilesFromUserByID(@PathVariable String userId) throws UserNotFoundException {
        return userRepository.findById(userId).map(user -> fileRepository.findByOwner(user)).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping("/{userId}/files")
    public UserFile saveFile(@PathVariable String userId, @RequestBody UserFile userFile) throws UserNotFoundException {
        return userRepository.findById(userId).map(user -> {
            userFile.setOwner(user);
            user.setFiles(List.of(userFile));
            userRepository.save(user);
            return fileRepository.save(userFile);
        }).orElseThrow(UserNotFoundException::new);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}
