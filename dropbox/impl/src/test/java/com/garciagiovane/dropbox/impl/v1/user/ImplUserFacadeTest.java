package com.garciagiovane.dropbox.impl.v1.user;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.service.UserService;
import com.garciagiovane.dropbox.impl.v1.user.stubs.FileStub;
import com.garciagiovane.dropbox.impl.v1.user.stubs.ShareModelStub;
import com.garciagiovane.dropbox.impl.v1.user.stubs.UserModelStub;
import com.garciagiovane.dropbox.impl.v1.user.stubs.ViewerStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImplUserFacadeTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private ImplUserFacade userFacade;

    @Test
    public void shouldCreateUserAndSave() {
        when(userService.create(UserModelStub.getUserModel())).thenReturn(UserModelStub.getUserModel());

        UserModel user = userFacade.create(UserModelStub.getUserModel());
        assertEquals(user, UserModelStub.getUserModel());
    }

    @Test
    public void shouldFindByUserIdWhenPassIdExistent() {
        when(userService.findById("ownerId")).thenReturn(UserModelStub.getUserModel());

        UserModel user = userFacade.findById("ownerId");
        assertEquals(user.getName(), UserModelStub.getUserModel().getName());
    }

    @Test
    public void shouldReturnAPageWithUsers() {
        when(userService.findAllUsers(PageRequest.of(0, 2))).thenReturn(new PageImpl<>(List.of(UserModelStub.getUserModel(), UserModelStub.getUserModelDifferent())));
        when(userService.findAllUsers(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(List.of(UserModelStub.getUserModel())));

        Page<UserModel> users = userFacade.findAllUsers(PageRequest.of(0, 2));
        assertEquals(2, users.getTotalElements());
        assertEquals(UserModelStub.getUserModel(), users.getContent().get(0));

        Page<UserModel> usersFound = userFacade.findAllUsers(PageRequest.of(0, 1));
        assertEquals(1, usersFound.getTotalElements());
    }

    @Test
    public void shouldUpdateUserWhenPassIdAndUserExisting() {
        when(userService.updateUser("someId", UserModelStub.getUserModel())).thenReturn(UserModelStub.getUserModelDifferent());

        UserModel userUpdated = userFacade.updateUser("someId", UserModelStub.getUserModel());
        assertEquals(userUpdated.getName(), UserModelStub.getUserModelDifferent().getName());
        assertEquals(userUpdated.getId(), UserModelStub.getUserModelDifferent().getId());
    }

    @Test
    public void removeFileFromUser() {
        userFacade.removeFileFromUser("someId", "fileId");
        verify(userService, times(1)).removeFileFromUser("someId", "fileId");
    }

    @Test
    public void shouldDddFileToUserAndSaveUser() {
        when(userService.addFilesToUser("someId", FileStub.getFileModel())).thenReturn(FileStub.getFileModel());

        FileModel file = userFacade.addFileToUser("someId", FileStub.getFileModel());
        assertEquals("fileId", file.getId());
    }

    @Test
    public void shouldDeleteUser() {
        userFacade.deleteUser("someId");
        verify(userService, times(1)).deleteUser("someId");
    }

    @Test
    public void shouldAddViewerToFileAndReturnFile() {
        FileModel file = FileStub.getFileModel();
        file.setViewers(List.of(ViewerStub.getViewer()));
        when(userService.shareFile("someId", ShareModelStub.getShareModel())).thenReturn(file);

        FileModel fileReturned = userFacade.shareFile("someId", ShareModelStub.getShareModel());
        assertEquals(1, fileReturned.getViewers().size());
    }
}