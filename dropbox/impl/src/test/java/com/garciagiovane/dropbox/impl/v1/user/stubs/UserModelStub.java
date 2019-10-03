package com.garciagiovane.dropbox.impl.v1.user.stubs;

import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;

import java.util.ArrayList;

public class UserModelStub {

    public static UserModel getUserModel() {
        return UserModel.builder()
                .id("someId")
                .name("teste")
                .email("teste@teste.com")
                .files(new ArrayList<>())
                .build();
    }

    public static UserModel getUserModelDifferent() {
        return UserModel.builder()
                .id("someId")
                .name("Iron Man")
                .email("iron@man.com")
                .files(new ArrayList<>())
                .build();
    }
}
