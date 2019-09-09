package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.repository.FileRepository;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class FileRestController {
    private FileRepository fileRepository;
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    public FileRestController(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}/files")
    public List<UserFile> getAllFilesFromUserByID(@PathVariable String userId) throws Exception {
        return fileService.getAllFilesFromUserByID(userId);
    }

    @PostMapping("/{userId}/files")
    public UserFile saveFile(@PathVariable String userId, @RequestBody UserFile userFile) throws UserNotFoundException {
        return fileService.saveFile(userId, userFile);
    }

    @ExceptionHandler({UserNotFoundException.class, NoFilesFoundException.class})
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }

    private List<UserFile> addItemsToListOfFiles(List<UserFile> files, UserFile newFile){
        files.add(newFile);
        return files;
    }
}
