package com.garciagiovane.upload.rest;

import com.garciagiovane.upload.ftp.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;

@RestController
@RequestMapping(value = "files")
public class FileRest {
    private FTPService ftp;

    @Autowired
    public FileRest(FTPService ftp) {
        this.ftp = ftp;
    }

    @PostMapping(value = "")
    public ResponseEntity getMessageSuccess(@RequestParam("file") MultipartFile file) {
        try {
            ftp.saveFile(file);
            return ResponseEntity.noContent().build();
        } catch (IOException | MultipartException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "a file is needed", e);
        }
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity getFile(@PathVariable(value = "fileName") String fileName) {
        try {
            InputStreamResource inputStreamResource = new InputStreamResource(ftp.searchFileByName(fileName));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(inputStreamResource);
        } catch (IOException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "file not found", e);
        }
    }

    @DeleteMapping(value = "/{fileName}")
    public ResponseEntity deleteFile(@PathVariable("fileName") String fileName){
        try {
            ftp.deleteFile(fileName);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "file not found", e);
        }
    }

    @GetMapping(value = "")
    public ResponseEntity listFiles() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ftp.getAllFiles());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error listing files");
        }
    }
}