package com.garciagiovane.upload.fileRest;

import com.garciagiovane.upload.exceptions.StorageServiceFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@RestController
@RequestMapping(value = "files")
public class FileRest {
    private StorageService storageService;

    @Autowired
    public FileRest(StorageService storage) {
        this.storageService = storage;
    }

    @PostMapping(value = "")
    public ResponseEntity getMessageSuccess(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("need file");
        else {
            return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
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
