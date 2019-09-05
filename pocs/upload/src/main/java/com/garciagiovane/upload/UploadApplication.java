package com.garciagiovane.upload;

import com.garciagiovane.upload.exceptions.CantSaveFileException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class UploadApplication {
    public static void main(String[] args) throws IOException, CantSaveFileException {
        SpringApplication.run(UploadApplication.class, args);
    }
}
