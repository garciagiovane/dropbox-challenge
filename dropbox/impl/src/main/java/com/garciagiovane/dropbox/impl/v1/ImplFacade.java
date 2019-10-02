package com.garciagiovane.dropbox.impl.v1;

import com.garciagiovane.dropbox.impl.v1.file.ImplFileFacade;
import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.user.ImplUserFacade;
import com.garciagiovane.dropbox.impl.v1.user.model.ShareModel;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ImplFacade {
    private ImplUserFacade userFacade;
    private ImplFileFacade fileFacade;

    public UserModel create(UserModel userModel) {
        return userFacade.create(userModel);
    }

    public UserModel findById(String id) {
        return userFacade.findById(id);
    }

    public Page<UserModel> findAllUsers(Pageable pageable) {
        return userFacade.findAllUsers(pageable);
    }

    public UserModel updateUser(String id, UserModel userModel) {
        return userFacade.updateUser(id, userModel);
    }

    public void deleteUser(String id) {
        deleteDirectory(id);
        userFacade.deleteUser(id);
    }

    public FileModel saveFile(String ownerId, MultipartFile fileToSave) {
        return userFacade.addFileToUser(ownerId, fileFacade.saveFile(findById(ownerId), fileToSave));
    }

    public Page<ImplFTPFile> searchFiles(String ownerId, String fileName, Pageable pageable) {
        return fileFacade.searchFiles(findById(ownerId), fileName, pageable);
    }

    public void deleteFile(String ownerId, String fileId) {
        fileFacade.deleteFile(findById(ownerId), fileId);
        userFacade.removeFileFromUser(ownerId, fileId);
    }

    private void deleteDirectory(String ownerId) {
        fileFacade.deleteDirectory(findById(ownerId));
    }

    public void shareFile(String ownerId, ShareModel shareModel) {
        fileFacade.saveFileShared(userFacade.shareFile(ownerId, shareModel));
    }
}
