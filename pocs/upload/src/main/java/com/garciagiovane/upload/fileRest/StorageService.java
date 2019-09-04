package com.garciagiovane.upload.fileRest;

import com.garciagiovane.upload.exceptions.StorageServiceFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getRootLocation());
    }

    public Resource getFileByName(String fileName) throws MalformedURLException, StorageServiceFileNotFoundException {
        Path file = rootLocation.resolve(fileName);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists())
            return resource;
        else
            throw new StorageServiceFileNotFoundException("File not found");
    }


}
