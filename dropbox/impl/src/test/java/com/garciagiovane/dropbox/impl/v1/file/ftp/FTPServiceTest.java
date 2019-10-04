package com.garciagiovane.dropbox.impl.v1.file.ftp;

import com.garciagiovane.dropbox.impl.v1.file.exception.FTPException;
import com.garciagiovane.dropbox.impl.v1.user.exception.ApiException;
import com.garciagiovane.dropbox.impl.v1.user.stubs.UserModelStub;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RunWith(MockitoJUnitRunner.class)
public class FTPServiceTest {

    @Mock
    private FTPConnection ftpConnection;

    @InjectMocks
    private FTPService service;

    private FTPFile[] directories;

    @Before
    public void directories() {
        FTPFile directory = new FTPFile();
        directory.setName("someId");

        directories = new FTPFile[1];
        directories[0] = directory;
    }

    @Test
    public void shouldReturnTrueWhenUserDirectoryExists() throws IOException {
        when(ftpConnection.getInstance()).thenReturn(mock(FTPClient.class));
        when(ftpConnection.getInstance().listDirectories()).thenReturn(directories);

        assertTrue(service.directoryExists(UserModelStub.getUserModel()));
    }

    @Test(expected = FTPException.class)
    public void shouldThrowExceptionWhenFTPServerIsNotUp() {
        when(ftpConnection.getInstance()).thenReturn(new FTPClient());
        service.directoryExists(UserModelStub.getUserModel());
    }

//    @Test
//    public void shouldSaveFileSuccessfullyWhenTryToSaveFileAndDirectoryExists() throws IOException {
//        MockMultipartFile fileToSave = new MockMultipartFile("file", "file.txt", "text/plain", "content".getBytes());
//
//        when(ftpConnection.getInstance()).thenReturn(mock(FTPClient.class));
//        when(ftpConnection.getInstance().listDirectories()).thenReturn(directories);
//        when(ftpConnection.getInstance().changeWorkingDirectory(UserModelStub.getUserModel().getId())).thenReturn(true);
//
//        when(ftpConnection.getInstance().storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream())).thenReturn(true);
//
//        service.saveFile(UserModelStub.getUserModel(), fileToSave);
//        verify(ftpConnection.getInstance(), times(1)).storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
//    }

    @Test
    public void shouldRenameFileWithNoExceptions() throws IOException {
        when(ftpConnection.getInstance()).thenReturn(mock(FTPClient.class));
        when(ftpConnection.getInstance().changeWorkingDirectory(UserModelStub.getUserModel().getId())).thenReturn(true);
        when(ftpConnection.getInstance().rename("someId-test.txt", "someId-newTest.txt")).thenReturn(true);

        service.renameFile(UserModelStub.getUserModel(), "someId-test.txt", "someId-newTest.txt");
        verify(ftpConnection.getInstance(), times(1)).rename("someId-test.txt", "someId-newTest.txt");
    }
}
