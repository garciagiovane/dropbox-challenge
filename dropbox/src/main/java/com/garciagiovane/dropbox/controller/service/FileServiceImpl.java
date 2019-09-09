package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.repository.FileRepository;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;
    private UserRepository userRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository){
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<UserFile> getAllFilesFromUserByID(String idOwner) throws Exception {
        List<UserFile> files = fileRepository.findByIdOwner(idOwner);
        if (!files.isEmpty())
            return files;
        throw new NoFilesFoundException();
    }

    @Override
    public UserFile saveFile(String userId, UserFile userFile) throws UserNotFoundException {
        return userRepository.findById(userId).map(userFound -> {
            userFile.setIdOwner(userId);

            userFound.setFiles(addItemsToListOfFiles(userFound.getFiles(), userFile));
            userRepository.save(userFound);
            return fileRepository.save(userFile);
        }).orElseThrow(UserNotFoundException::new);
    }

    private List<UserFile> addItemsToListOfFiles(List<UserFile> files, UserFile newFile){
        files.add(newFile);
        return files;
    }
}
