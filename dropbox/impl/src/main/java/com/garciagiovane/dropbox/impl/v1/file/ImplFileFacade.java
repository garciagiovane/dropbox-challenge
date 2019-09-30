package com.garciagiovane.dropbox.impl.v1.file;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ImplFileFacade {
    private FileService fileService;

    public FileModel saveFile(String ownerId, MultipartFile fileToSave){
        return fileService.saveFile(ownerId, fileToSave);
    }

    public Page<ImplFTPFile> searchFiles(String ownerId, String fileName, Pageable pageable){
        return fileService.searchFileByName(ownerId, fileName, pageable);
    }
}
