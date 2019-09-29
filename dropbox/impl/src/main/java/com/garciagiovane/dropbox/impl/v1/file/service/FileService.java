package com.garciagiovane.dropbox.impl.v1.file.service;

import com.garciagiovane.dropbox.impl.v1.ImplFacade;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPErrorExitingException;
import com.garciagiovane.dropbox.impl.v1.file.ftp.FTPService;
import com.garciagiovane.dropbox.impl.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.file.repository.FileRepository;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
@AllArgsConstructor
public class FileService {

    private FTPService ftpService;
    private FileRepository fileRepository;
    private ImplFacade implFacade;

    public FileModel saveFile(String ownerId, MultipartFile fileToSave){
        UserModel user = implFacade.findById(ownerId);
        try {
            ftpService.saveFile(user,fileToSave);
            FileModel fileSaved = FileMapper.mapToFileModel(fileRepository.save(FileMapper.mapToEntity(FileModel.builder()
                    .idOwner(ownerId)
                    .originalName(fileToSave.getOriginalFilename())
                    .viewers(Collections.emptyList())
                    .ftpName(ownerId + "-" + fileToSave.getOriginalFilename())
                    .build())));
            ftpService.renameFile(user, fileSaved.getOriginalName(), fileSaved.getFtpName());
            return fileSaved;
        } catch (IOException e) {
            throw new FTPErrorExitingException(e.getMessage());
        }
    }
}
