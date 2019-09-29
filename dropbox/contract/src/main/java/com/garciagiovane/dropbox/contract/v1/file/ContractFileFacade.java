package com.garciagiovane.dropbox.contract.v1.file;

import com.garciagiovane.dropbox.contract.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.impl.v1.file.FileFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ContractFileFacade {

    private FileFacade fileFacade;

    public FileResponse saveFile(String ownerId, MultipartFile fileToSave){
        return FileMapper.mapToContract(fileFacade.saveFile(ownerId, fileToSave));
    }
}
