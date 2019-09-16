package com.garciagiovane.dropbox.controller.service;

import com.garciagiovane.dropbox.dto.UserFileDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public UserFile saveFile(String userId, MultipartFile multipartFile) throws UserNotFoundException, IOException, ConnectionRefusedException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (ftpService.saveFile(multipartFile, user.getId())) {
            UserFile userFile = fileRepository.save(UserFile.builder()
                    .idOwner(userId)
                    .originalName(multipartFile.getOriginalFilename())
                    .viewers(Collections.emptyList())
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
        fileRepository.deleteById(fileId);

        user.setFiles(removeItemsFromListOfFiles(user.getFiles(), fileToRemove));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @Override
    public void deleteDatabaseFiles(List<UserFile> files) {
        fileRepository.deleteAll(files);
    }

    @Override
    public UserFile updateFile(UserFile userFile) {
        return fileRepository.save(userFile);
    }

    public Page<UserFileDTO> getFilesByName(String userId, Optional<String> fileName, Pageable pageable) throws UserNotFoundException {
        if (userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException();

        try {
            return fileName.isPresent() ?
                    getAllFilesByUserIdFTPFiltered(userId, fileName.get(), pageable) :
                    getAllFilesByUserIdFTP(userId, pageable);
        } catch (DirectoryNotFoundException | IOException | ConnectionRefusedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> Page<T> paginateList(List<T> listToPaginate, Pageable pageable){
        int itemsQuantity = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = itemsQuantity * currentPage;

        List<T> files;
        if (listToPaginate.size() < startItem)
            files = List.of();
        else {
            int toIndex = Math.min(startItem + itemsQuantity, listToPaginate.size());
            files = listToPaginate.subList(startItem, toIndex);
        }
        return new PageImpl<>(files, pageable, listToPaginate.size());
    }

    private Page<UserFileDTO> getAllFilesByUserIdFTPFiltered(String userId, String fileName, Pageable pageable) throws DirectoryNotFoundException, IOException, ConnectionRefusedException {
        List<UserFileDTO> filesFound = ftpService.getAllFilesByUserId(userId).stream().filter(userFileDTO -> userFileDTO.getCompleteName().contains(fileName)).collect(Collectors.toList());
        return paginateList(filesFound, pageable);
    }

    private Page<UserFileDTO> getAllFilesByUserIdFTP(String userId, Pageable pageable) throws DirectoryNotFoundException, IOException, ConnectionRefusedException {
        List<UserFileDTO> filesFound = ftpService.getAllFilesByUserId(userId);
        return paginateList(filesFound, pageable);
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

    @Override
    public boolean deleteDirectory(String userId) throws ConnectionRefusedException, IOException {
        return ftpService.deleteDirectory(userId);
    }
}