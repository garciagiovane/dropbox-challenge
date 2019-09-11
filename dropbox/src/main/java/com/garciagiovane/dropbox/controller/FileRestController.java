package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class FileRestController {
    private FileService fileService;

    @Autowired
    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public List<UserFile> getAllFiles(){
        return fileService.getAllFiles();
    }

    @GetMapping("/{userId}/files")
    public List<UserFile> getAllFilesFromUserByID(@PathVariable String userId) throws Exception {
        return fileService.getAllFilesFromUserByID(userId);
    }

    @GetMapping("/{userId}/files/{fileName}")
    public List<UserFile> getFilesByName(@PathVariable String userId, @PathVariable String fileName) throws UserNotFoundException, NoFilesFoundException {
        return fileService.getFilesByName(userId, fileName);
    }

    @PostMapping("/{userId}/files")
    public UserFile saveFile(@PathVariable String userId, @RequestParam MultipartFile file) throws UserNotFoundException {
        return fileService.saveFile(userId, file);
    }

    @DeleteMapping("/{userId}/files/{fileId}")
    public ResponseEntity deleteFileById(@PathVariable String userId, @PathVariable String fileId) throws UserNotFoundException {
        return fileService.deleteFileById(userId, fileId);
    }

    @ExceptionHandler({UserNotFoundException.class, NoFilesFoundException.class})
    public ResponseEntity exceptionHandler() {
        return ResponseEntity.notFound().build();
    }
}
