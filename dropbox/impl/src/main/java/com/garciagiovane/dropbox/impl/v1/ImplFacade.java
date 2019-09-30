package com.garciagiovane.dropbox.impl.v1;

import com.garciagiovane.dropbox.impl.v1.file.ImplFileFacade;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImplFacade {
    private UserService userService;
//    @Autowired
//    private ImplFileFacade fileFacade;

    public UserModel create(UserModel userModel){
        return userService.create(userModel);
    }

    public UserModel findById(String id){
        return userService.findById(id);
    }

    public Page<UserModel> findAllUsers(Pageable pageable){
        return userService.findAllUsers(pageable);
    }

    public UserModel updateUser(String id, UserModel userModel){
        return userService.updateUser(id, userModel);
    }

    public boolean deleteUser(String id){
        return userService.deleteUser(id);
    }
}
