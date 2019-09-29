package com.garciagiovane.dropbox.impl.v1.user.mapper;

import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserEntity;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class UserMapperTest {
    private UserEntity entity = UserEntity.builder()
            .id("someId")
            .name("user")
            .email("user@mail.com")
            .files(Collections.emptyList())
            .build();
    private UserModel model = UserModel.builder()
            .id(entity.getId())
            .name(entity.getName())
            .email(entity.getEmail())
            .files(entity.getFiles())
            .build();

    @Test
    public void mapToModel() {
        assertEquals(model.getName(), UserMapper.mapToModel(entity).getName());
        assertEquals(model.getEmail(), UserMapper.mapToModel(entity).getEmail());
        assertEquals(model.getId(), UserMapper.mapToModel(entity).getId());
        assertEquals(model.getFiles(), UserMapper.mapToModel(entity).getFiles());
    }

    @Test
    public void mapToEntity() {
        assertEquals(entity.getName(), UserMapper.mapToEntity(model).getName());
        assertEquals(entity.getEmail(), UserMapper.mapToEntity(model).getEmail());
        assertEquals(entity.getId(), UserMapper.mapToEntity(model).getId());
        assertEquals(entity.getFiles(), UserMapper.mapToEntity(model).getFiles());
    }
}