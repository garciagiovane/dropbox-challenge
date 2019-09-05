package com.garciagiovane.upload.configuration;

import com.garciagiovane.upload.ftp.FTPServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "com.garciagiovane.upload")
public class ConfigurationBean {
    @Bean
    @Scope(value = "prototype")
    public FTPServiceImpl ftpServiceImpl(){
        return new FTPServiceImpl();
    }
}
