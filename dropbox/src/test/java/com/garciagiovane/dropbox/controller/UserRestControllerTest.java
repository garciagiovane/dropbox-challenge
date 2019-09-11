package com.garciagiovane.dropbox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garciagiovane.dropbox.controller.service.UserService;
import static org.junit.Assert.assertThat;

import com.garciagiovane.dropbox.dto.UserDTO;
import com.garciagiovane.dropbox.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Victoria");
        userDTO.setEmail("vic@mail.com");
        MvcResult result = this.mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(userDTO))).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
