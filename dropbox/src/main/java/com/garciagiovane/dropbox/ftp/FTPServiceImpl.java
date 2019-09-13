package com.garciagiovane.dropbox.ftp;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.DirectoryNotFoundException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FTPServiceImpl implements FTPService {
    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.username}")
    private String userName;

    @Value("${ftp.password}")
    private String password;

    private FTPClient ftpClientInstance() throws ConnectionRefusedException {
        try {
            FTPClient ftpClient = new FTPClient();

            ftpClient.connect(host);
            ftpClient.login(userName, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
            return ftpClient;
        }catch (Exception error){
            throw new ConnectionRefusedException(error.getMessage());
        }
    }

    @Override
    public Page<String> getAllFilesByUserId(String userId) throws DirectoryNotFoundException, ConnectionRefusedException {
        try {
            FTPClient ftpClient = ftpClientInstance();

            if (directoryExists(userId)){
                ftpClient.changeWorkingDirectory(userId);
                List<String> files = Arrays.stream(ftpClient.listFiles()).map(FTPFile::getName).collect(Collectors.toList());
                return new PageImpl<>(files);
            }
        } catch (ConnectionRefusedException | IOException e) {
            throw new ConnectionRefusedException(e.getMessage());
        }
        throw new DirectoryNotFoundException();
    }

    @Override
    public boolean deleteFile(String fileName, String userId) throws DirectoryNotFoundException, ConnectionRefusedException {
        try {
            FTPClient ftpClient = ftpClientInstance();
            if (directoryExists(userId)){
                ftpClient.changeWorkingDirectory(userId);
                return ftpClient.deleteFile(fileName);
            }
        } catch (IOException | ConnectionRefusedException e){
            throw new ConnectionRefusedException(e.getMessage());
        }
        throw new DirectoryNotFoundException();
    }

    @Override
    public boolean directoryExists(String userId) throws ConnectionRefusedException, IOException {
        FTPFile[] directories = ftpClientInstance().listDirectories();
        long listDirs = Arrays.stream(directories).filter(ftpFile -> ftpFile.getName().equalsIgnoreCase(userId)).count();
        return listDirs > 0;
    }

    @Override
    public boolean saveFile(MultipartFile fileToSave, String userId) throws ConnectionRefusedException {
        try {
            FTPClient ftpClient = ftpClientInstance();

            if (directoryExists(userId)){
                ftpClient.changeWorkingDirectory(userId);
                return ftpClient.storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
            }
            ftpClient.makeDirectory(userId);
            ftpClient.changeWorkingDirectory(userId);
            return ftpClient.storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
        } catch (ConnectionRefusedException | IOException e) {
            throw new ConnectionRefusedException(e.getMessage());
        }
    }

    @Override
    public boolean renameFile(String originalFileName, String newFileName, String ownerId) throws IOException, ConnectionRefusedException {
        FTPClient ftpClient = ftpClientInstance();
        ftpClient.changeWorkingDirectory(ownerId);
        return ftpClient.rename(originalFileName, newFileName);
    }
}
