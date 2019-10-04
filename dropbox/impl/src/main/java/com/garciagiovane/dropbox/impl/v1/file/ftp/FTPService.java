package com.garciagiovane.dropbox.impl.v1.file.ftp;

import com.garciagiovane.dropbox.impl.v1.file.exception.FTPErrorSavingFileException;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPDirectoryNotFoundException;
import com.garciagiovane.dropbox.impl.v1.file.exception.FTPException;
import com.garciagiovane.dropbox.impl.v1.file.mapper.FileMapper;
import com.garciagiovane.dropbox.impl.v1.file.model.ImplFTPFile;
import com.garciagiovane.dropbox.impl.v1.user.model.UserModel;
import lombok.AllArgsConstructor;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FTPService {
    private FTPConnection ftp;

    public boolean directoryExists(UserModel owner) {
        try {
            long directory = Arrays.stream(ftp.getInstance().listDirectories()).filter(ftpFolder -> ftpFolder.getName().equalsIgnoreCase(owner.getId())).count();
            return directory > 0;
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        }
    }

    public void saveFile(UserModel owner, MultipartFile fileToSave) throws IOException {
        FTPClient ftpClient = ftp.getInstance();
        try {
            if (directoryExists(owner)) {
                ftpClient.changeWorkingDirectory(owner.getId());
                ftpClient.storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
            }
            ftpClient.makeDirectory(owner.getId());
            ftpClient.changeWorkingDirectory(owner.getId());
            ftpClient.storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
        } catch (IOException e) {
            throw new FTPErrorSavingFileException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public void renameFile(UserModel owner, String nameSavedOnFTP, String newName) throws IOException {
        FTPClient ftpClient = ftp.getInstance();
        try {
            ftpClient.changeWorkingDirectory(owner.getId());
            if (!ftpClient.rename(nameSavedOnFTP, newName))
                throw new FTPException("Error renaming file");
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public List<ImplFTPFile> searchFileByName(UserModel owner) throws IOException {
        FTPClient ftpClient = ftp.getInstance();
        try {
            if (directoryExists(owner)) {
                ftpClient.changeWorkingDirectory(owner.getId());
                return Arrays.stream(ftpClient.listFiles()).map(FileMapper::mapToFTPFile).collect(Collectors.toList());
            }
            throw new FTPDirectoryNotFoundException();
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public boolean deleteFile(UserModel user, String fileName) throws IOException {
        FTPClient ftpClient = ftp.getInstance();
        try {
            if (directoryExists(user)) {
                ftpClient.changeWorkingDirectory(user.getId());
                return ftpClient.deleteFile(fileName);
            }
            throw new FTPDirectoryNotFoundException();
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public boolean deleteDirectory(UserModel user) throws IOException {
        FTPClient ftpClient = ftp.getInstance();
        try {
            if (directoryExists(user)) {

                return ftpClient.removeDirectory(user.getId());
            }
            return false;
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
}
