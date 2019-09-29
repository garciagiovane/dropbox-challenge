package com.garciagiovane.dropbox.impl.v1.file;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class FileFacade {
    private FileService fileService;

    public FileModel saveFile(String ownerId, MultipartFile fileToSave){
        return fileService.saveFile(ownerId, fileToSave);
    }
}
