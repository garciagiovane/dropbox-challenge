package com.garciagiovane.dropbox.impl.v1.user.service;

import com.garciagiovane.dropbox.impl.v1.user.exception.EmptyDatabaseException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserExistsException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserNotFoundException;
import com.garciagiovane.dropbox.impl.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserEntity;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserModel findById(String id) {
        return UserMapper.mapToModel(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    private boolean userExists(UserModel userModel){
        return userRepository.findByEmail(userModel.getEmail()).isPresent();
    }

    public UserModel create(UserModel user) {
        if (userExists(user))
            throw new UserExistsException();
        return UserMapper.mapToModel(userRepository.save(UserMapper.mapToEntity(user)));
    }

    public Page<UserModel> findAllUsers(Pageable pageable){
        Page<UserEntity> usersFound = userRepository.findAll(pageable);
        if (usersFound.isEmpty())
            throw new EmptyDatabaseException();
        return usersFound.map(UserMapper::mapToModel);
    }

    public UserModel updateUser(String id, UserModel userModel){
        return UserMapper.mapToModel(userRepository.findById(id).map(userEntity -> {
            if (userExists(userModel) && !userModel.getEmail().equalsIgnoreCase(userEntity.getEmail()))
                throw new UserExistsException();
            userEntity.setName(userModel.getName());
            userEntity.setEmail(userModel.getEmail());
            if (!userModel.getFiles().isEmpty())
                userEntity.setFiles(userModel.getFiles());
            return userRepository.save(userEntity);
        }).orElseThrow(UserNotFoundException::new));
    }

    public boolean deleteUser(String id){
        return userRepository.findById(id).map(userEntity -> {
            userRepository.delete(userEntity);
            return true;
        }).orElseThrow(UserNotFoundException::new);
    }
}
