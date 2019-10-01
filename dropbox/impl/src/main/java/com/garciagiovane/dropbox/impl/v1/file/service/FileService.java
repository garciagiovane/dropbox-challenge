package com.garciagiovane.dropbox.impl.v1.file.service;

import com.garciagiovane.dropbox.impl.v1.user.ImplUserFacade;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPErrorDeletingFileException;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPErrorExitingException;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPFileNotFoundException;
import com.garciagiovane.dropbox.impl.v1.file.ftp.FTPService;
import com.garciagiovane.dropbox.impl.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.file.repository.FileRepository;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {

    private FTPService ftpService;
    private FileRepository fileRepository;
    private ImplUserFacade implUserFacade;

    private <T> Page<T> paginateList(List<T> listToPaginate, Pageable pageable) {
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

    private List<FileModel> addItemsToListOfFiles(List<FileModel> files, FileModel newFile) {
        files.add(newFile);
        return files;
    }

    private List<FileModel> removeItemsFromListOfFiles(List<FileModel> files, FileModel fileToRemove) {
        files.remove(fileToRemove);
        return files;
    }

    public FileModel saveFile(String ownerId, MultipartFile fileToSave) {
        UserModel user = implUserFacade.findById(ownerId);
        try {
            ftpService.saveFile(user, fileToSave);
            FileModel fileSaved = FileMapper.mapToFileModel(fileRepository.save(FileMapper.mapToEntity(FileModel.builder()
                    .idOwner(ownerId)
                    .originalName(fileToSave.getOriginalFilename())
                    .viewers(Collections.emptyList())
                    .build())));
            fileSaved.setFtpName(fileSaved.getId() + "-" + fileToSave.getOriginalFilename());
            ftpService.renameFile(user, fileSaved.getOriginalName(), fileSaved.getFtpName());
            user.setFiles(addItemsToListOfFiles(user.getFiles(), fileSaved));
            implUserFacade.updateUser(user.getId(), user);
            return fileSaved;
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }

    public Page<ImplFTPFile> searchFileByName(String ownerId, String fileName, Pageable pageable) {
        UserModel user = implUserFacade.findById(ownerId);
        if (fileName == null)
            return paginateList(listAllFilesFTPFromUser(user), pageable);
        return paginateList(listAllFilesFTPFromUser(user).stream().filter(implFTPFile -> implFTPFile.getName().contains(fileName)).collect(Collectors.toList()), pageable);
    }

    private List<ImplFTPFile> listAllFilesFTPFromUser(UserModel user) {
        try {
            List<ImplFTPFile> filesFound = ftpService.searchFileByName(user);
            if (filesFound.isEmpty())
                throw new FTPFileNotFoundException();
            return filesFound;
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }

    public boolean deleteFile(String ownerId, String fileId) {
        UserModel user = implUserFacade.findById(ownerId);
        FileModel file = getFileFromList(user.getFiles(), fileId);
        try {
            if (ftpService.deleteFile(user, file.getFtpName())) {
                user.setFiles(removeItemsFromListOfFiles(user.getFiles(), file));
                implUserFacade.updateUser(ownerId, user);
                return true;
            }
            throw new FTPErrorDeletingFileException();
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }

    public boolean deleteDirectory(String ownerId) {
        UserModel user = implUserFacade.findById(ownerId);
        if (ftpService.directoryExists(user)) {
            listAllFilesFTPFromUser(user).forEach(file -> {
                try {
                    ftpService.deleteFile(user, file.getName());
                } catch (IOException e) {
                    throw new FTPErrorExitingException(e.getMessage());
                }
            });
            try {
                if (ftpService.deleteDirectory(user))
                    return true;
                throw new FTPErrorDeletingFileException();
            } catch (IOException e) {
                throw new FTPErrorExitingException(e.getMessage());
            }
        }
        return true;
    }

    private FileModel getFileFromList(List<FileModel> files, String fileId) {
        return files.stream().filter(file -> file.getId().equalsIgnoreCase(fileId)).findFirst().orElseThrow(FTPFileNotFoundException::new);
    }
}
