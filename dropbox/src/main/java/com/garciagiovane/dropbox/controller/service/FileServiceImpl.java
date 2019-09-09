package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.ftp.FTPService;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.repository.FileRepository;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private FTPService ftpService;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, FTPService ftpService) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.ftpService = ftpService;
    }

    @Override
    public List<UserFile> getAllFilesFromUserByID(String idOwner) throws Exception {
        List<UserFile> files = fileRepository.findByIdOwner(idOwner);
        if (!files.isEmpty())
            return files;
        throw new NoFilesFoundException();
    }

    @Override
    public UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException {
        return userRepository.findById(userId).map(userFound -> {
            UserFile userFile = new UserFile();
            try {
                ftpService.saveFile(multipartFile);
                userFile = fileRepository.save(UserFile.builder().idOwner(userId).completeName(multipartFile.getOriginalFilename()).build());

                userFound.setFiles(addItemsToListOfFiles(userFound.getFiles(), userFile));
                userRepository.save(userFound);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userFile;
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserFile> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public ResponseEntity deleteFileById(String userId, String fileId) throws UserNotFoundException {
        return userRepository.findById(userId).map(user -> {
            try {
                UserFile fileToRemove = fileRepository.findById(fileId).orElseThrow(NoFilesFoundException::new);

                user.setFiles(removeItemsFromListOfFiles(user.getFiles(), fileToRemove));
                userRepository.save(user);
                fileRepository.delete(fileToRemove);
                ftpService.deleteFile(fileToRemove.getCompleteName());
            } catch (NoFilesFoundException | IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.noContent().build();
        }).orElseThrow(UserNotFoundException::new);
    }

    private List<UserFile> addItemsToListOfFiles(List<UserFile> files, UserFile newFile) {
        files.add(newFile);
        return files;
    }

    private List<UserFile> removeItemsFromListOfFiles(List<UserFile> files, UserFile fileToRemove) {
        files.remove(fileToRemove);
        return files;
    }
}
