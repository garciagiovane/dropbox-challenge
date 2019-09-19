package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.controller.service.FileService;
import com.garciagiovane.dropbox.dto.UserFileDTO;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return a list all files in the database")
    })
    @GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFile> getAllFiles(Pageable pageable) {
        return fileService.getAllFiles(pageable);
    }

    @ApiOperation(value = "return a list of files the user has in the FTP server according to the user id, you can pass name parameter to filter by name or pass page and size parameters to paginate, can also pass both")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return a list of all files from the user"),
            @ApiResponse(code = 404, message = "exception occurred, user not found or files not found")
    })
    @GetMapping(value = "/{userId}/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<UserFileDTO> getFilesByUserId(@ApiParam(value = "user id", required = true) @PathVariable String userId, @ApiParam(value = "file name") @RequestParam Optional<String> name, Pageable pageable) throws UserNotFoundException, NoFilesFoundException {
        return fileService.getFilesByName(userId, name, pageable);
    }

    @ApiOperation(value = "Posting a file at the file parameter and passing the user id from the owner you will create a doc file this user and will receive a json representation for that file that was stored in an database, you need to send a multipartfile at the 'file' parameter")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "return JSON representing the file created"),
            @ApiResponse(code = 404, message = "exception occurred, user not found"),
            @ApiResponse(code = 500, message = "exception occurred, connection to ftp refused")
    })
    @PostMapping(value = "/{userId}/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserFile saveFile(@ApiParam(value = "user id") @PathVariable String userId, @ApiParam(value = "multipart file") @RequestParam MultipartFile file) throws UserNotFoundException, IOException, ConnectionRefusedException {
        return fileService.saveFile(userId, file);
    }

    @ApiOperation(value = "Do a delete request passing the file owner id and the file id you will delete the file from the ftp server and database")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "no message will be returned"),
            @ApiResponse(code = 404, message = "exception occurred, user not found, file not found or ftp directory not found"),
            @ApiResponse(code = 500, message = "exception occurred, access to ftp refused"),
    })
    @DeleteMapping("/{userId}/files/{fileId}")
    public ResponseEntity deleteFileById(@ApiParam(value = "owner's file id", required = true) @PathVariable String userId, @ApiParam(value = "file id", required = true) @PathVariable String fileId) throws UserNotFoundException, IOException, NoFilesFoundException, ConnectionRefusedException, DirectoryNotFoundException {
        return fileService.deleteFileById(userId, fileId);
    }
}