package com.garciagiovane.dropbox.ftp;

import com.garciagiovane.dropbox.exception.ConnectionRefusedException;
import com.garciagiovane.dropbox.exception.UserNotFoundException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            throw new ConnectionRefusedException();
        }
    }

    @Override
    public boolean saveFile(MultipartFile fileToSave) throws IOException, ConnectionRefusedException {
        return ftpClientInstance().storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
    }

    @Override
    public boolean deleteFile(String fileName) throws IOException, ConnectionRefusedException {
        return ftpClientInstance().deleteFile(fileName);
    }

    @Override
    public boolean renameFile(String originalFileName, String newFileName) throws IOException, ConnectionRefusedException {
        return ftpClientInstance().rename(originalFileName, newFileName);
    }
}
