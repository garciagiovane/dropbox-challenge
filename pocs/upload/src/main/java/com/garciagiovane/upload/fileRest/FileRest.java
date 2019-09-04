package com.garciagiovane.upload.fileRest;

import com.garciagiovane.upload.exceptions.CantSaveFileException;
import com.garciagiovane.upload.exceptions.StorageServiceFileNotFoundException;
import com.garciagiovane.upload.ftp.FTPService;
import com.garciagiovane.upload.ftp.FTPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;

@RestController
@RequestMapping(value = "files")
public class FileRest {
    private StorageService storageService;
    private FTPService ftp;

    @Autowired
    public FileRest(StorageService storage) {
        this.storageService = storage;
        this.ftp = new FTPServiceImpl();
    }

    @PostMapping(value = "")
    public ResponseEntity getMessageSuccess(@RequestParam("file") MultipartFile file) {
        try {
            ftp.saveFile(file);
            return ResponseEntity.noContent().build();
        } catch (IOException | CantSaveFileException e) {
            Map<String, String> errors = Map.of("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }

    @GetMapping(value = "/{fileName}")
    public ResponseEntity getFile(@PathVariable(value = "fileName") String fileName) {
        try {
            Resource resource = storageService.getFileByName(fileName);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } catch (MalformedURLException | StorageServiceFileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }
    }

    @ExceptionHandler(StorageServiceFileNotFoundException.class)
    public ResponseEntity<?> handlerFileNotFound(FileNotFoundException error) {
        return ResponseEntity.notFound().build();
    }
}
