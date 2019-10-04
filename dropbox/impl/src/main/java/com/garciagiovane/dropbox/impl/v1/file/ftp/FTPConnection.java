package com.garciagiovane.dropbox.impl.v1.file.ftp;

import com.garciagiovane.dropbox.impl.v1.file.exception.FTPException;
import lombok.Setter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Setter
public class FTPConnection {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.port}")
    private int port;

    FTPClient getInstance() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
            return ftpClient;
        } catch (IOException e) {
            throw new FTPException(e.getMessage());
        }
    }
}
