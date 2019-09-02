package com.garciagiovane.upload.fileRest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping(value = "files")
public class FileRest {

    @PostMapping(value = "")
    public ResponseEntity getMessageSuccess(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("need file");
        else {
            Path target = Paths.get("/home/ftp");
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
        }
    }
}
