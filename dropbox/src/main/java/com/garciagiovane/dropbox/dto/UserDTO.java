package com.garciagiovane.dropbox.dto;

import com.garciagiovane.dropbox.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;

    public User user(){
        return User.builder().name(this.name).email(this.email).build();
    }
}
