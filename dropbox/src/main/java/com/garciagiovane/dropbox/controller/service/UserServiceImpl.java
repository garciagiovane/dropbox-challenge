package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserAlreadyRegisteredException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.model.Viewer;
import com.garciagiovane.dropbox.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FileService fileService;

    @Override
    public User createUser(UserDTO userDTO) throws UserAlreadyRegisteredException {
        if (validateUser(userDTO.user())) {
            User user = userDTO.user();
            user.setFiles(Collections.emptyList());
            return userRepository.save(user);
        }
        throw new UserAlreadyRegisteredException("email already in use");
    }

    @Override
    public boolean validateUser(User user) {
        return userRepository.findByEmail(user.getEmail()).isEmpty();
    }

    @Override
    public User getUserById(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUserById(UserDTO userDTO, String id) throws UserNotFoundException, UserAlreadyRegisteredException {
        User userFound = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!validateUser(userFound) && userDTO.getEmail().equalsIgnoreCase(userFound.getEmail())){
            User user = userDTO.user();
            user.setId(id);
            user.setFiles(userFound.getFiles());
            return userRepository.save(user);
        }
        throw new UserAlreadyRegisteredException();
    }

    @Override
    public ResponseEntity deleteUserById(String id) throws UserNotFoundException, ConnectionRefusedException, IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        fileService.deleteDirectory(user.getId());
        fileService.deleteDatabaseFiles(user.getFiles());
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
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

        if (result) {
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
