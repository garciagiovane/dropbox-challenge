package com.garciagiovane.upload;

import com.garciagiovane.upload.ftp.FTPService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadAndDownloadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FTPService ftpService;

    @Test
    public void shouldListFiles() throws Exception {
        given(this.ftpService.getAllFiles()).willReturn(List.of("first.txt", "second.txt"));
        MvcResult result = this.mockMvc.perform(get("/files")).andExpect(status().isOk()).andReturn();
        assert result.getResponse().getContentAsString().equalsIgnoreCase("[\"first.txt\",\"second.txt\"]");
    }

    @Test
    public void shouldSaveFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        this.mockMvc.perform(multipart("/files").file(multipartFile)).andExpect(status().isNoContent());
        then(this.ftpService).should().saveFile(multipartFile);
    }

    @Test
    public void shouldSearchFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("message".getBytes());

        given(this.ftpService.searchFileByName("test.txt")).willReturn(inputStream);
        this.mockMvc.perform(get("/files/test.txt")).andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        given(this.ftpService.deleteFile("test.txt")).willReturn(true);
        this.mockMvc.perform(delete("/files/test.txt")).andExpect(status().isNoContent());
    }

    @Test
    public void shouldThrowExceptionSearchFileByName() throws Exception {
        given(this.ftpService.deleteFile("test.txt")).willThrow(IOException.class);
        this.mockMvc.perform(delete("/files/test.txt")).andExpect(status().isNotFound());
    }
}
