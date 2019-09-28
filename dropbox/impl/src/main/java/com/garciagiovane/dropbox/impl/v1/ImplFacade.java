package com.garciagiovane.dropbox.impl.v1;

import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImplFacade {
    private UserService userService;

    public UserModel create(UserModel userModel){
        return userService.create(userModel);
    }

}
