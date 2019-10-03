package com.garciagiovane.dropbox.impl.v1.user.service;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.user.exception.*;
import com.garciagiovane.dropbox.impl.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.impl.v1.user.model.ShareModel;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.model.Viewer;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserEntity;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserModel findById(String id) {
        return UserMapper.mapToModel(userRepository.findById(id).orElseThrow(() -> {
            UserNotFoundException userNotFoundException = new UserNotFoundException();
            throw new ApiException(List.of(userNotFoundException), HttpStatus.NOT_FOUND);
        }));
    }

    private boolean userExists(UserModel userModel) {
        return userRepository.findByEmail(userModel.getEmail()).isPresent();
    }

    public UserModel create(UserModel user) {
        if (userExists(user))
            throw new ApiException(List.of(new UserExistsException()), HttpStatus.BAD_REQUEST);
        return UserMapper.mapToModel(userRepository.save(UserMapper.mapToEntity(user)));
    }

    public Page<UserModel> findAllUsers(Pageable pageable) {
        Page<UserEntity> usersFound = userRepository.findAll(pageable);
        if (usersFound.isEmpty())
            throw new EmptyDatabaseException();
        return usersFound.map(UserMapper::mapToModel);
    }

    public UserModel updateUser(String id, UserModel userModel) {
        UserModel user = findById(id);
        if (userExists(userModel) && !userModel.getEmail().equalsIgnoreCase(user.getEmail()))
            throw new UserExistsException();

        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        return UserMapper.mapToModel(userRepository.save(UserMapper.mapToEntity(user)));
    }

    public void removeFileFromUser(String userId, String fileId) {
        UserModel user = findById(userId);
        FileModel file = user.getFiles().stream().filter(fileModel -> fileModel.getId().equals(fileId)).findFirst().orElseThrow(FileNotFoundException::new);
        user.setFiles(removeFilesFromList(user.getFiles(), file));
        userRepository.save(UserMapper.mapToEntity(user));
    }

    private List<FileModel> removeFilesFromList(List<FileModel> files, FileModel fileToRemove) {
        files.remove(fileToRemove);
        return files;
    }

    public FileModel addFilesToUser(String userId, FileModel file) {
        UserModel userModel = UserMapper.mapToModel(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
        userModel.setFiles(addFileToList(userModel.getFiles(), file));
        userRepository.save(UserMapper.mapToEntity(userModel));
        return file;
    }

    private List<FileModel> addFileToList(List<FileModel> files, FileModel fileToAdd) {
        files.add(fileToAdd);
        return files;
    }

    public void deleteUser(String id) {
        userRepository.delete(UserMapper.mapToEntity(findById(id)));
    }

    private List<Viewer> addViewerToList(List<Viewer> viewers, Viewer viewer) {
        viewers.add(viewer);
        return viewers;
    }

    public FileModel shareFile(String ownerId, ShareModel shareModel) {
        UserModel owner = findById(ownerId);
        UserModel userRecipient = findById(shareModel.getUserRecipientId());
        FileModel fileToShare = owner.getFiles().stream().filter(file -> file.getId().equals(shareModel.getFileId())).peek(fileModel -> fileModel.setViewers(addViewerToList(fileModel.getViewers(), UserMapper.mapToViewer(userRecipient)))).findFirst().orElseThrow(FileNotFoundException::new);
        userRepository.save(UserMapper.mapToEntity(owner));
        return fileToShare;
    }
}
