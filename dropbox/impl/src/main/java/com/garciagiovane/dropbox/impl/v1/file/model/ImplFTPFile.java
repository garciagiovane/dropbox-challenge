package com.garciagiovane.dropbox.impl.v1.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImplFTPFile {
    private String name;
    private long size;
}
