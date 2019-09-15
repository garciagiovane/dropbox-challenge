package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class FileRestController {
    private FileService fileService;

    @Autowired
    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @ApiOperation(value = "Return the list of all files saved on the database")
    @GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFile> getAllFiles(Pageable pageable) {
        return fileService.getAllFiles(pageable);
    }

    @ApiOperation(value = "return a list of files according to the user id, you can pass name parameter to filter by name or pass page and size parameters to paginate, can also pass both")
    @GetMapping(value = "/{userId}/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFile> getFilesByUserId(@PathVariable String userId, @RequestParam Optional<String> name, Pageable pageable) throws UserNotFoundException, NoFilesFoundException {
        return fileService.getFilesByName(userId, name, pageable);
    }

    @ApiOperation(value = "Posting a file at the file parameter and passing the user id from the owner you will create a doc file this user and will receive a json representation for that file that was stored in an database")
    @PostMapping(value = "/{userId}/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserFile saveFile(@PathVariable String userId, @RequestParam MultipartFile file) throws UserNotFoundException, IOException, ConnectionRefusedException {
        return fileService.saveFile(userId, file);
    }

    @ApiOperation(value = "Do a delete request passing the file owner id and the file id you will delete the file from the ftp server and database")
    @DeleteMapping("/{userId}/files/{fileId}")
    public ResponseEntity deleteFileById(@PathVariable String userId, @PathVariable String fileId) throws UserNotFoundException, IOException, NoFilesFoundException, ConnectionRefusedException, DirectoryNotFoundException {
        return fileService.deleteFileById(userId, fileId);
    }
}