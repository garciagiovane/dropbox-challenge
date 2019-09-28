package com.garciagiovane.dropbox.impl.v1.user.service;

import com.garciagiovane.dropbox.impl.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository repository;

    public UserModel findById(String id) {
        return UserMapper.mapToModel(repository.findById(id).orElseThrow(InputMismatchException::new));
    }

    public UserModel create(UserModel user) {
        return UserMapper.mapToModel(repository.save(UserMapper.mapToEntity(user)));
    }
}
