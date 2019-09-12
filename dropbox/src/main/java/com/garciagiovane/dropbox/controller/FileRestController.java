package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/users")
public class FileRestController {
    private FileService fileService;

    @Autowired
    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public Page<UserFile> getAllFiles(Pageable pageable){
        return fileService.getAllFiles(pageable);
    }

    @GetMapping("/{userId}/files")
    public Page<UserFile> getAllFilesFromUserByID(@PathVariable String userId, Pageable pageable) throws NoFilesFoundException {
        return fileService.getAllFilesFromUserByID(userId, pageable);
    }

    @GetMapping("/{userId}/files/{fileName}")
    public Page<UserFile> getFilesByName(@PathVariable String userId, @PathVariable String fileName, Pageable pageable) throws UserNotFoundException, NoFilesFoundException {
        return fileService.getFilesByName(userId, fileName, pageable);
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
