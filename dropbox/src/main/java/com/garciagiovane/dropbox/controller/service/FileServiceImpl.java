package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import com.garciagiovane.dropbox.exception.NoFilesFoundException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import com.garciagiovane.dropbox.ftp.FTPService;
import com.garciagiovane.dropbox.model.User;
import com.garciagiovane.dropbox.model.UserFile;
import com.garciagiovane.dropbox.repository.FileRepository;
import com.garciagiovane.dropbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<String> getAllFilesFromUserByID(String idOwner, Pageable pageable) throws NoFilesFoundException, DirectoryNotFoundException, IOException, ConnectionRefusedException {
        Page<String> filesFound = ftpService.getAllFilesByUserId(idOwner);
        if (filesFound.isEmpty())
            throw new NoFilesFoundException();

        return filesFound;
    }

    @Override
    public UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException, IOException, ConnectionRefusedException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (ftpService.saveFile(multipartFile, user.getId())){
            UserFile userFile = fileRepository.save(UserFile.builder()
                    .idOwner(userId)
                    .originalName(multipartFile.getOriginalFilename())
                    .build());

            renameFtpFileAndUpdateFileName(userFile);
            user.setFiles(addItemsToListOfFiles(user.getFiles(), userFile));
            userRepository.save(user);
            return userFile;
        }
        throw new ConnectionRefusedException();
    }

    @Override
    public Page<UserFile> getAllFiles(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity deleteFileById(String userId, String fileId) throws UserNotFoundException, NoFilesFoundException, IOException, ConnectionRefusedException, DirectoryNotFoundException {
        UserFile fileToRemove = fileRepository.findById(fileId).orElseThrow(NoFilesFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        ftpService.deleteFile(fileToRemove.getFtpName(), user.getId());
        user.setFiles(removeItemsFromListOfFiles(user.getFiles(), fileToRemove));
        userRepository.save(user);
        fileRepository.delete(fileToRemove);

        return ResponseEntity.noContent().build();
    }

    @Override
    public UserFile updateFile(UserFile userFile) {
        return fileRepository.save(userFile);
    }

    @Override
    public Page<UserFile> getFilesByName(String userId, String fileName, Pageable pageable) throws UserNotFoundException, NoFilesFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page<UserFile> filesFound = fileRepository.findByOriginalNameContaining(fileName, pageable);
        if (filesFound.isEmpty()) {
            throw new NoFilesFoundException();
        }
        return filesFound;
    }

    private List<UserFile> addItemsToListOfFiles(List<UserFile> files, UserFile newFile) {
        files.add(newFile);
        return files;
    }

    private List<UserFile> removeItemsFromListOfFiles(List<UserFile> files, UserFile fileToRemove) {
        files.remove(fileToRemove);
        return files;
    }

    private void renameFtpFileAndUpdateFileName(UserFile userFile) throws IOException, ConnectionRefusedException {
        userFile.setFtpName(userFile.getId() + "-" + userFile.getOriginalName());
        ftpService.renameFile(userFile.getOriginalName(), userFile.getFtpName(), userFile.getIdOwner());
        fileRepository.save(userFile);
    }
}
