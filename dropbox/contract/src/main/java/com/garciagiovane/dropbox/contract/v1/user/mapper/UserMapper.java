package com.garciagiovane.dropbox.contract.v1.user.mapper;

import com.garciagiovane.dropbox.contract.v1.user.model.request.UserRequest;
import com.garciagiovane.dropbox.contract.v1.user.model.response.UserResponse;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;

import java.util.Collections;

public class UserMapper {

    public static UserResponse mapToContract(UserModel userModel) {
        return UserResponse.builder()
                .id(userModel.getId())
                .name(userModel.getName())
                .email(userModel.getEmail())
                .files(userModel.getFiles())
                .build();
    }

    public static UserModel mapToImpl(UserRequest userRequest) {
        return UserModel.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .files(Collections.emptyList())
                .build();
    }
}
