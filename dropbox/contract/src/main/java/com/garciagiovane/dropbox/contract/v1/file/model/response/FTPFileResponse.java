package com.garciagiovane.dropbox.contract.v1.file.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FTPFileResponse {
    private String name;
    private long size;
}
