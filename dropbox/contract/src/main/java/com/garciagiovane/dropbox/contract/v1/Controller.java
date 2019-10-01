package com.garciagiovane.dropbox.contract.v1;

import com.garciagiovane.dropbox.contract.v1.file.model.response.FTPFileResponse;
import com.garciagiovane.dropbox.contract.v1.file.model.response.FileResponse;
import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/v1/users")
@RestController
@AllArgsConstructor
public class Controller {

    private ContractFacade contractFacade;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest userRequest){
        return contractFacade.create(userRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponse findUserById(@PathVariable String id){
        return contractFacade.findById(id);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<UserResponse> findAllUsers(Pageable pageable){
        return contractFacade.findAllUsers(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserRequest userRequest){
        return contractFacade.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable String id){
        return contractFacade.deleteUser(id);
    }

    @PostMapping("/{id}/files")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FileResponse saveFile(@PathVariable String id, @RequestParam MultipartFile fileToSave){
        return contractFacade.saveFile(id, fileToSave);
    }

    @GetMapping("/{ownerId}/files")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<FTPFileResponse> searchFiles(@PathVariable String ownerId, @RequestParam(required = false) String fileName, Pageable pageable) {
        return contractFacade.searchFiles(ownerId, fileName, pageable);
    }

    @DeleteMapping("/{ownerId}/files/{fileId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public boolean deleteFile(@PathVariable String ownerId, @PathVariable String fileId) {
        return contractFacade.deleteFile(ownerId, fileId);
    }
}
