package com.garciagiovane.upload.ftp;

import com.garciagiovane.upload.exceptions.CantSaveFileException;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FTPServiceImpl implements FTPService {
    private final FTPClient ftpClient;

    public FTPServiceImpl(){
        this.ftpClient = new FTPClient();
        connect("172.17.0.2");
        login("giovane", "giovane");
    }

    @Override
    public void connect(String host) {
        try {
            ftpClient.connect(host);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(String userName, String password) {
        try {
            if (ftpClient.login(userName, password)) {
                System.out.println("login success");
            } else
                System.out.println("login failed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream searchFileByName(String fileName) throws IOException {
        InputStream fileToServe = ftpClient.retrieveFileStream(fileName);
        return fileToServe;
    }

    public void listDirectories() {
        try {
            String[] files = ftpClient.listNames();

            for (String file : files)
                System.out.println(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean saveFile(MultipartFile fileToSave) throws IOException, CantSaveFileException {
        if (ftpClient.storeFile(fileToSave.getOriginalFilename(), fileToSave.getInputStream()))
            return true;
        else
            throw new CantSaveFileException("Error saving file");
    }
}
