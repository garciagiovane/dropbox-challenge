package com.garciagiovane.dropbox.impl.v1.user.mapper;

import com.garciagiovane.dropbox.stubs.UserEntityStub;
import com.garciagiovane.dropbox.stubs.UserModelStub;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserMapperTest {

    @Test
    public void mapToModel() {
        assertEquals(UserModelStub.getUserModel().getName(), UserMapper.mapToModel(UserEntityStub.getUserEntity()).getName());
        assertEquals(UserModelStub.getUserModel().getEmail(), UserMapper.mapToModel(UserEntityStub.getUserEntity()).getEmail());
        assertEquals(UserModelStub.getUserModel().getId(), UserMapper.mapToModel(UserEntityStub.getUserEntity()).getId());
        assertEquals(UserModelStub.getUserModel().getFiles(), UserMapper.mapToModel(UserEntityStub.getUserEntity()).getFiles());
    }

    @Test
    public void mapToEntity() {
        assertEquals(UserEntityStub.getUserEntity().getName(), UserMapper.mapToEntity(UserModelStub.getUserModel()).getName());
        assertEquals(UserEntityStub.getUserEntity().getEmail(), UserMapper.mapToEntity(UserModelStub.getUserModel()).getEmail());
        assertEquals(UserEntityStub.getUserEntity().getId(), UserMapper.mapToEntity(UserModelStub.getUserModel()).getId());
        assertEquals(UserEntityStub.getUserEntity().getFiles(), UserMapper.mapToEntity(UserModelStub.getUserModel()).getFiles());
    }
}