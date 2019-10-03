package com.garciagiovane.dropbox.impl.v1.user.stubs;

import com.garciagiovane.dropbox.impl.v1.user.repository.UserEntity;

import java.util.ArrayList;

public class UserEntityStub {

    public static UserEntity getUserEntity() {
        return UserEntity.builder()
                .id("someId")
                .name("teste")
                .email("teste@teste.com")
                .files(new ArrayList<>())
                .build();
    }

    public static UserEntity getUserEntityDifferent() {
        return UserEntity.builder()
                .id("someId")
                .name("Iron Man")
                .email("iron@man.com")
                .files(new ArrayList<>())
                .build();
    }
}
