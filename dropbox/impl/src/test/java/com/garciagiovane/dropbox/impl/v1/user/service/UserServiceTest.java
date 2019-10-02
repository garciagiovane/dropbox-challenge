package com.garciagiovane.dropbox.impl.v1.user.service;

import com.garciagiovane.dropbox.impl.v1.file.model.FileModel;
import com.garciagiovane.dropbox.impl.v1.user.exception.EmptyDatabaseException;
import com.garciagiovane.dropbox.impl.v1.user.exception.FileNotFoundException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserExistsException;
import com.garciagiovane.dropbox.impl.v1.user.exception.UserNotFoundException;
import com.garciagiovane.dropbox.impl.v1.user.mapper.UserMapper;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserEntity;
import com.garciagiovane.dropbox.impl.v1.user.repository.UserRepository;
import com.garciagiovane.dropbox.stubs.FileStub;
import com.garciagiovane.dropbox.stubs.ShareModelStub;
import com.garciagiovane.dropbox.stubs.UserEntityStub;
import com.garciagiovane.dropbox.stubs.UserModelStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {


    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    public void findById() {
        when(repository.findById("someId")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));
        UserModel model = userService.findById("someId");
        verify(repository, times(1)).findById("someId");
        assertEquals(UserModelStub.getUserModel(), model);
    }

    @Test(expected = UserNotFoundException.class)
    public void deveLancarExceptionQuandoNaoEncontrarPeloId() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());
        userService.findById("someId");
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionWhenUserIsNotFound() {
        when(repository.findById(anyString())).thenThrow(UserNotFoundException.class);
        userService.findById("someId");
    }

    @Test
    public void willReturnUserSuccessfullyAfterSave() {
        when(repository.findByEmail("teste@teste.com")).thenReturn(Optional.empty());
        when(repository.save(UserEntityStub.getUserEntity())).thenReturn(UserEntityStub.getUserEntity());

        UserModel userModel = userService.create(UserModelStub.getUserModel());
        assertEquals(UserModelStub.getUserModel(), userModel);
    }

    @Test(expected = UserExistsException.class)
    public void willThrowExceptionWhenModelEmailIsEqualToTheModelEmailPassedByParameter() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));
        userService.create(UserModelStub.getUserModel());
    }

    @Test
    public void shouldReturnAllUsersFromTheDatabase() {
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(UserEntityStub.getUserEntity())));
        Pageable pageable = PageRequest.of(0, 1);

        Page<UserModel> allUsers = userService.findAllUsers(pageable);
        assertEquals(1, allUsers.getContent().size());
        assertEquals(UserModelStub.getUserModel(), allUsers.getContent().get(0));
    }

    @Test(expected = EmptyDatabaseException.class)
    public void shouldThrowExceptionWhenThereIsNoUsersInTheDatabase() {
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        Pageable pageable = PageRequest.of(0, 1);
        userService.findAllUsers(pageable);
    }

    @Test
    public void willChangeTheUserNameWithSuccessAfterSave() {
        when(repository.findById("someId")).thenReturn(Optional.of(UserEntityStub.getUserEntity()));
        when(repository.save(UserEntityStub.getUserEntity())).thenReturn(UserEntityStub.getUserEntityDifferent());

        UserModel userModel = userService.updateUser("someId", UserModelStub.getUserModel());
        assertEquals(UserMapper.mapToModel(UserEntityStub.getUserEntityDifferent()), userModel);
    }

    @Test(expected = UserExistsException.class)
    public void shouldThrowExceptionAfterPassModelWithEmailAlreadyRegistered() {
        when(repository.findById("someId")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));
        when(repository.findByEmail("iron@man.com")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntityDifferent()));

        userService.updateUser("someId", UserModelStub.getUserModelDifferent());
    }

    @Test
    public void deleteUser() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));
        userService.deleteUser("qualquer");
        verify(repository, times(1)).delete(any());
    }

    @Test
    public void shouldRemoveFileFromListOfFilesFromUserAndSaveUser() {
        UserEntity userEntity = UserEntityStub.getUserEntity();
        userEntity.setFiles(FileStub.getListOfFiles());

        when(repository.findById("someId")).thenReturn(Optional.of(userEntity));
        when(repository.save(userEntity)).thenReturn(userEntity);

        userService.removeFileFromUser(UserModelStub.getUserModel().getId(), "fileId");
        verify(repository, times(1)).findById("someId");
    }

    @Test
    public void shouldAddFileToUserAndSaveUser() {
        when(repository.findById("someId")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));
        when(repository.save(any())).thenReturn(any());

        FileModel fileSaved = userService.addFilesToUser("someId", FileStub.getFileModel());
        assertEquals(fileSaved.getFtpName(), FileStub.getFileModel().getFtpName());
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowFileNotFoundExceptionWhenFileIsNotInTheDatabase() {
        when(repository.findById("someId")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntity()));

        userService.removeFileFromUser("someId", "fileId");
    }

    @Test
    public void shouldShareAFileThenSaveAUserAndReturnAFileModel() {
        UserEntity userEntity = UserEntityStub.getUserEntity();
        userEntity.setFiles(List.of(FileStub.getFileModel()));

        when(repository.findById("ownerId")).thenReturn(Optional.of(userEntity));
        when(repository.findById("recipientId")).thenReturn(Optional.ofNullable(UserEntityStub.getUserEntityDifferent()));
        when(repository.save(any())).thenReturn(any());

        FileModel file = userService.shareFile("ownerId", ShareModelStub.getShareModel());
        assertEquals(file.getViewers().get(0).getName(), UserEntityStub.getUserEntityDifferent().getName());
    }
}