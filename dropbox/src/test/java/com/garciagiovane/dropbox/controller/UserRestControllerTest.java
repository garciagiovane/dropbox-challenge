package com.garciagiovane.dropbox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garciagiovane.dropbox.controller.service.UserService;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.model.ShareEntity;
import com.garciagiovane.dropbox.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        given(this.userService.createUser(UserDTO.builder().name("victória").email("vic@mail.com").build()))
                .willReturn(User.builder().id("someid").name("test").email("test@mail.com").build());

        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .writeValueAsString(UserDTO
                                .builder()
                                .name("victória")
                                .email("vic@mail.com").build())))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value("someid"));
    }

    @Test
    public void getUserById() throws Exception {
        given(this.userService.getUserById("someid")).willReturn(User.builder().id("someid").name("test").email("test@mail.com").build());
        this.mockMvc.perform(get("/users/someid")).andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    public void updateUserById() throws Exception {
        given(this.userService.updateUserById(UserDTO.builder().name("giovane").email("giovane@.mail.com").build(), "someid"))
                .willReturn(User.builder().id("someid").name("giovane-alterado").email("giovane@mail.com").build());
        this.mockMvc.perform(put("/users/someid").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(UserDTO.builder().name("giovane").email("giovane@.mail.com").build())))
                .andExpect(jsonPath("$.name").value("giovane-alterado"));
    }

    @Test
    public void deleteUserById() throws Exception {
        given(this.userService.deleteUserById("someid")).willReturn(ResponseEntity.noContent().build());
        this.mockMvc.perform(delete("/users/someid")).andExpect(status().isNoContent());
    }

    @Test
    public void createShare() throws Exception {
        given(this.userService.shareFileById("someid", new ShareEntity("fileid", "userToShareWithId")))
                .willReturn(ResponseEntity.noContent().build());
        this.mockMvc.perform(post("/users/someid")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper()
                        .writeValueAsString(new ShareEntity("fileid", "userToShareWithId")))).andExpect(status().isNoContent());
    }
}
