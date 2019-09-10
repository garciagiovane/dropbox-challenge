package com.garciagiovane.dropbox.ftp;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FTPServiceImplTest {

    @MockBean
    private FTPService ftpService;

    @Test
    public void saveFileTest() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "testing".getBytes());
        given(this.ftpService.saveFile(mockMultipartFile)).willReturn(true);
        assertTrue(this.ftpService.saveFile(mockMultipartFile));
    }

    @Test
    public void deleteFile() throws IOException {
        given(this.ftpService.deleteFile("test.txt")).willReturn(true);
        assertTrue(this.ftpService.deleteFile("test.txt"));
    }

    @Test
    public void renameFile() throws IOException {
        String initialFileName = "test.txt";
        String newFileName = "newtest.txt";

        given(this.ftpService.renameFile(initialFileName, newFileName)).willReturn(true);
        assertTrue(this.ftpService.renameFile(initialFileName, newFileName));
    }

    @Test(expected = IOException.class)
    public void deleteFileThrowIOException() throws IOException {
        given(this.ftpService.deleteFile("test.txt")).willThrow(IOException.class);
        assertTrue(this.ftpService.deleteFile("test.txt"));
    }

}
