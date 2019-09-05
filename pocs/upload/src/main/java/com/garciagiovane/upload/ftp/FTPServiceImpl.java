package com.garciagiovane.upload.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FTPServiceImpl implements FTPService {
    @Value("${my.hostname}")
    private String hostName;

    @Value("${my.username}")
    private String userName;

    @Value("${my.password}")
    private String password;

    private FTPClient ftpClientInstance() throws IOException {
        FTPClient ftpClient = new FTPClient();

        ftpClient.connect(hostName);
        ftpClient.login(userName, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
        return ftpClient;
    }

    @Override
    public InputStream searchFileByName(String fileName) throws IOException {
        return ftpClientInstance().retrieveFileStream(fileName);
    }

    @Override
    public boolean saveFile(MultipartFile fileToSave) throws IOException {
        return ftpClientInstance().storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream());
    }

    @Override
    public boolean deleteFile(String fileName) throws IOException {
        return ftpClientInstance().deleteFile(fileName);
    }

    @Override
    public List<String> getAllFiles() throws IOException {
        return Arrays.stream(ftpClientInstance().listFiles()).map(FTPFile::getName).collect(Collectors.toList());
    }
}
