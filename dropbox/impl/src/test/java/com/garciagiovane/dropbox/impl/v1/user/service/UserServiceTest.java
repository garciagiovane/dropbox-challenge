package com.garciagiovane.dropbox.impl.v1.user.service;

import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserService userService;
    private UserModel userModel;

    @Before
    public void setup(){
        userModel = UserModel.builder()
                .id("someId")
                .name("teste")
                .email("teste@teste.com")
                .files(Collections.emptyList())
                .build();
    }

    @Test
    public void findById() {
        given(this.userService.findById("someId")).willReturn(userModel);
        assertEquals("teste@teste.com", this.userService.findById("someId").getEmail());
    }

    @Test
    public void create() {
        given(this.userService.create(userModel)).willReturn(userModel);
        assertEquals(userModel, this.userService.create(userModel));
        assertEquals("teste", this.userService.create(userModel).getName());
    }

    @Test
    public void findAllUsers() {
        Page<UserModel> users = new PageImpl<>(Collections.singletonList(userModel));
        Pageable pageable = PageRequest.of(0,1);
        given(this.userService.findAllUsers(pageable)).willReturn(users);
        assertEquals(1, this.userService.findAllUsers(pageable).getTotalElements());
        List<UserModel> user = this.userService.findAllUsers(pageable).getContent();
        assertEquals("teste", user.get(0).getName());
    }

    @Test
    public void updateUser() {
        UserModel userUpdated = userModel;
        userUpdated.setName("updated");
        userUpdated.setEmail("updated@test.com");
        given(this.userService.updateUser("someId", userModel)).willReturn(userUpdated);
        assertEquals(userUpdated.getName(), this.userService.updateUser("someId", userModel).getName());
        assertEquals(userUpdated.getEmail(), this.userService.updateUser("someId", userModel).getEmail());
    }

    @Test
    public void deleteUser() {
        when(this.userService.deleteUser("someId")).thenReturn(true);
        assertTrue(this.userService.deleteUser("someId"));
    }
}