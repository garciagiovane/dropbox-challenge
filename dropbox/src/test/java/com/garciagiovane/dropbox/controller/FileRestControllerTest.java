package com.garciagiovane.dropbox.controller;

import com.garciagiovane.dropbox.model.UserFile;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRestController fileRestController;

    @Test
    public void getAllFiles() throws Exception {
        UserFile file = UserFile.builder().id("someid").ftpName("someid-test.txt").idOwner("ownerId").originalName("test.txt").build();
        Page<UserFile> page = new PageImpl<>(Collections.singletonList(file));
        given(this.fileRestController.getAllFiles(PageRequest.of(0, 1))).willReturn(page);
        this.mockMvc.perform(get("/users/files?page=0&size=1")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void deleteFileById() throws Exception {
        given(this.fileRestController.deleteFileById("ownerId", "fileId")).willReturn(ResponseEntity.noContent().build());
        this.mockMvc.perform(delete("/users/ownerId/files/fileId")).andExpect(status().isNoContent());
    }

    @Test
    public void getFilesByUserId() throws Exception {
        UserFile file1 = UserFile.builder().id("someId").ftpName("someId-test.txt").idOwner("ownerId").originalName("test.txt").build();
        UserFile file2 = UserFile.builder().id("someId2").ftpName("someId2-test.txt").idOwner("ownerId").originalName("test2.txt").build();
        Page<UserFile> files = new PageImpl<>(Arrays.asList(file1, file2));

        given(this.fileRestController.getFilesByUserId("ownerId", Optional.of("t") , PageRequest.of(0, 2))).willReturn(files);
        this.mockMvc.perform(get("/users/ownerId/files?name=t&page=0&size=2")).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].originalName", containsInAnyOrder("test.txt", "test2.txt")));
    }

    @Test
    public void saveFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        given(this.fileRestController.saveFile("ownerId", multipartFile)).willReturn(UserFile.builder()
                .id("fileId").originalName("test.txt").ftpName("fileId-test.txt").idOwner("someId").build());
        this.mockMvc.perform(multipart("/users/ownerId/files").file(multipartFile)).andExpect(status().isOk()).andExpect(jsonPath("$.originalName").value("test.txt"));
    }
}
