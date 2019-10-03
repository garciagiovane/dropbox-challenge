package com.garciagiovane.dropbox.contract.v1;

import com.garciagiovane.dropbox.contract.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FTPFileResponse;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.contract.v1.user.mapper.ShareMapper;
import com.garciagiovane.dropbox.contract.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.contract.v1.user.model.request.ShareRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import com.garciagiovane.dropbox.impl.v1.ImplFacade;
import com.garciagiovane.dropbox.contract.v1.util.ImplValidation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ContractFacade {

    private ImplFacade implFacade;

    UserResponse create(UserRequest userRequest){
        ImplValidation.validateUserFields(userRequest);
        return UserMapper.mapToContract(implFacade.create(UserMapper.mapToImpl(userRequest)));
    }

    UserResponse findById(String id){
        return UserMapper.mapToContract(implFacade.findById(id));
    }

    Page<UserResponse> findAllUsers(Pageable pageable){
        return implFacade.findAllUsers(pageable).map(UserMapper::mapToContract);
    }

    UserResponse updateUser(String id, UserRequest userRequest){
        ImplValidation.validateUserFields(userRequest);
        return UserMapper.mapToContract(implFacade.updateUser(id, UserMapper.mapToImpl(userRequest)));
    }

    void deleteUser(String id){
        implFacade.deleteUser(id);
    }

    FileResponse saveFile(String ownerId, MultipartFile fileToSave) {
        return FileMapper.mapToContract(implFacade.saveFile(ownerId, fileToSave));
    }

    Page<FTPFileResponse> searchFiles(String ownerId, String fileName, Pageable pageable) {
        return implFacade.searchFiles(ownerId, fileName, pageable).map(FileMapper::mapToFTPFileResponse);
    }

    void deleteFile(String ownerId, String fileId) {
        implFacade.deleteFile(ownerId, fileId);
    }

    void shareFile(String ownerId, ShareRequest shareRequest) {
        implFacade.shareFile(ownerId, ShareMapper.mapToImpl(shareRequest));
    }
}
