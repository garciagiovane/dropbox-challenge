package com.garciagiovane.dropbox.dto;

import com.garciagiovane.dropbox.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;

    public User user(){
        return User.builder().name(this.name).email(this.email).build();
    }
}
