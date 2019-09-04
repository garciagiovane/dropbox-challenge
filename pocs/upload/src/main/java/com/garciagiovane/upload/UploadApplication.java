package com.garciagiovane.upload;

import com.garciagiovane.upload.exceptions.CantSaveFileException;
import com.garciagiovane.upload.fileRest.StorageProperties;
import com.garciagiovane.upload.ftp.FTPServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadApplication {
    public static void main(String[] args) throws IOException, CantSaveFileException {
        SpringApplication.run(UploadApplication.class, args);
//        FTPServiceImpl ftp = new FTPServiceImpl();
//
//        ftp.connect("172.17.0.2");
//        ftp.login("giovane", "giovane");
//        ftp.listDirectories();
//        ftp.saveFile();
    }
}
