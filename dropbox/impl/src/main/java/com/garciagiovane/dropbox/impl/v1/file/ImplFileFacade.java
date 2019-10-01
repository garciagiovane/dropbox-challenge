package com.garciagiovane.dropbox.impl.v1.file;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.file.service.FileService;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ImplFileFacade {
    private FileService fileService;

    public FileModel saveFile(UserModel owner, MultipartFile fileToSave){
        return fileService.saveFile(owner, fileToSave);
    }

    public Page<ImplFTPFile> searchFiles(UserModel owner, String fileName, Pageable pageable){
        return fileService.searchFileByName(owner, fileName, pageable);
    }

    public boolean deleteFile(UserModel owner, String fileId) {
        return fileService.deleteFile(owner, fileId);
    }

    public void deleteDirectory(UserModel owner) {
        fileService.deleteDirectory(owner);
    }
}
