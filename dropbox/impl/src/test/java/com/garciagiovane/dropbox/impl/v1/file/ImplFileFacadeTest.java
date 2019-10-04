package com.garciagiovane.dropbox.impl.v1.file;

import com.garciagiovane.dropbox.impl.v1.file.service.FileService;
import com.garciagiovane.dropbox.impl.v1.user.stubs.FileStub;
import com.garciagiovane.dropbox.impl.v1.user.stubs.UserModelStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static  org.mockito.Mockito.*;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class ImplFileFacadeTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private ImplFileFacade fileFacade;

    @Test
    public void shouldCallSaveFileMethodFromFileService() {
        MockMultipartFile fileToSave = new MockMultipartFile("file", "file.txt", "plain/text", "file content".getBytes());

        fileFacade.saveFile(UserModelStub.getUserModel(), fileToSave);
        verify(fileService, times(1)).saveFile(UserModelStub.getUserModel(), fileToSave);
    }

    @Test
    public void shouldCallSearchFileByNameMethodFromFileService() {
        fileFacade.searchFiles(UserModelStub.getUserModel(), "test", PageRequest.of(0, 1));
        verify(fileService, times(1)).searchFileByName(UserModelStub.getUserModel(), "test", PageRequest.of(0, 1));
    }

    @Test
    public void shouldCallDeleteFileMethodFromFileService() {
        fileFacade.deleteFile(UserModelStub.getUserModel(), "fileId");
        verify(fileService, times(1)).deleteFile(UserModelStub.getUserModel(), "fileId");
    }

    @Test
    public void shouldCallDeleteDirectoryMethodFromFileService() {
        fileFacade.deleteDirectory(UserModelStub.getUserModel());
        verify(fileService, times(1)).deleteDirectory(UserModelStub.getUserModel());
    }

    @Test
    public void shouldCallSaveFileSharedFromFileService() {
        fileFacade.saveFileShared(FileStub.getFileModel());
        verify(fileService, times(1)).saveFileShared(FileStub.getFileModel());
    }
}