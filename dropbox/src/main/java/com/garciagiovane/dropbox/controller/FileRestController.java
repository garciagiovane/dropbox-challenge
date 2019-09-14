package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.dto.UserFileDTO;
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

    @ApiOperation(value = "Passing the user id in the URI you will get all files from this user in the FTP server")
    @GetMapping(value = "/{userId}/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFileDTO> getAllFilesFromUserByID(@PathVariable String userId, Pageable pageable) throws NoFilesFoundException, DirectoryNotFoundException, IOException, ConnectionRefusedException {
        return fileService.getAllFilesFromUserByID(userId, pageable);
    }

    @ApiOperation(value = "Passing the user id and the file name or at least one character you will get the info about that docs or an exception for file not found")
    @GetMapping(value = "/{userId}/files/{fileName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFile> getFilesByName(@PathVariable String userId, @PathVariable String fileName, Pageable pageable) throws UserNotFoundException, NoFilesFoundException {
        return fileService.getFilesByName(userId, fileName, pageable);
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
