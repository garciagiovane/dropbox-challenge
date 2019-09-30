package com.garciagiovane.dropbox.contract.v1.file;

import com.garciagiovane.dropbox.contract.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FTPFileResponse;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.impl.v1.file.ImplFileFacade;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ContractFileFacade {

    private ImplFileFacade implFileFacade;

    public FileResponse saveFile(String ownerId, MultipartFile fileToSave) {
        return FileMapper.mapToContract(implFileFacade.saveFile(ownerId, fileToSave));
    }

    public Page<FTPFileResponse> searchFiles(String ownerId, String fileName, Pageable pageable) {
        return implFileFacade.searchFiles(ownerId, fileName, pageable).map(FileMapper::mapToFTPFileResponse);
    }

    public boolean deleteFile(String ownerId, String fileId) {
        return implFileFacade.deleteFile(ownerId, fileId);
    }
}
