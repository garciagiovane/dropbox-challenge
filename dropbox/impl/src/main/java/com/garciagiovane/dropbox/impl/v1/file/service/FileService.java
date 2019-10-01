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
import org.springframework.util.ObjectUtils;
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

    public FileModel saveFile(UserModel owner, MultipartFile fileToSave) {
        try {
            ftpService.saveFile(owner, fileToSave);
            FileModel fileSaved = FileMapper.mapToFileModel(fileRepository.save(FileMapper.mapToEntity(FileModel.builder()
                    .idOwner(owner.getId())
                    .originalName(fileToSave.getOriginalFilename())
                    .viewers(Collections.emptyList())
                    .build())));
            fileSaved.setFtpName(fileSaved.getId() + "-" + fileToSave.getOriginalFilename());
            ftpService.renameFile(owner, fileSaved.getOriginalName(), fileSaved.getFtpName());
            return fileSaved;
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }

    public Page<ImplFTPFile> searchFileByName(UserModel owner, String fileName, Pageable pageable) {
        if (fileName == null)
            return paginateList(listAllFilesFTPFromUser(owner), pageable);
        return paginateList(listAllFilesFTPFromUser(owner).stream().filter(implFTPFile -> implFTPFile.getName().contains(fileName)).collect(Collectors.toList()), pageable);
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

    public boolean deleteFile(UserModel owner, String fileId) {
        FileModel file = getFileFromList(owner.getFiles(), fileId);
        try {
            if (ftpService.deleteFile(owner, file.getFtpName())) {
                return true;
            }
            throw new FTPErrorDeletingFileException();
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }

    public void deleteDirectory(UserModel owner) {
        if (ftpService.directoryExists(owner)) {
            try {
                List<ImplFTPFile> filesFound = listAllFilesFTPFromUser(owner);
                if (!ObjectUtils.isEmpty(filesFound))
                    filesFound.forEach(file -> {
                        try {
                            ftpService.deleteFile(owner, file.getName());
                        } catch (IOException e) {
                            throw new FTPErrorExitingException(e.getMessage());
                        }
                    });

                if (!ftpService.deleteDirectory(owner))
                    throw new FTPErrorDeletingFileException();
            } catch (IOException e) {
                throw new FTPErrorExitingException(e.getMessage());
            }
        }
    }

    private FileModel getFileFromList(List<FileModel> files, String fileId) {
        return files.stream().filter(file -> file.getId().equalsIgnoreCase(fileId)).findFirst().orElseThrow(FTPFileNotFoundException::new);
    }
}
