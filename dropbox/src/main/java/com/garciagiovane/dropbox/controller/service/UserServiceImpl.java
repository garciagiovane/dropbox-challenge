package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.model.Viewer;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FileService fileService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        return userRepository.save(userDTO.user());
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUserById(UserDTO userDTO, String id) throws UserNotFoundException {
        return userRepository.findById(id).map(userFound -> {
            User user = userDTO.user();
            user.setId(id);
            user.setFiles(userFound.getFiles());
            return userRepository.save(user);
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public ResponseEntity deleteUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).map(user -> {
            user.getFiles().forEach(userFile -> {
                try {
                    fileService.deleteFileById(user.getId(), userFile.getId());
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                }
            });
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity shareFileById(String ownerId, ShareEntity shareEntity) throws UserNotFoundException, NoFilesFoundException {
        User owner = userExists(ownerId);
        User userToShareWith = userExists(shareEntity.getUserToShareWith());
        UserFile file = owner.getFiles().stream().filter(userFile -> userFile.getId().equals(shareEntity.getFileId())).findFirst().orElseThrow(NoFilesFoundException::new);

        Viewer viewer = Viewer.builder()
                .id(userToShareWith.getId())
                .name(userToShareWith.getName())
                .email(userToShareWith.getEmail())
                .build();

        file.setViewers(addItemsToListOfViewers(file.getViewers(), viewer));

        var result = owner.getFiles().stream().filter(userFile -> userFile.getId().equals(shareEntity.getFileId())).anyMatch(fileToUpdate -> {
            fileToUpdate = file;
            fileService.updateFile(fileToUpdate);
            return true;
        });

        if (result){
            userRepository.save(owner);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private List<Viewer> addItemsToListOfViewers(List<Viewer> viewers, Viewer newViewer) {
        viewers.add(newViewer);
        return viewers;
    }

    private User userExists(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
