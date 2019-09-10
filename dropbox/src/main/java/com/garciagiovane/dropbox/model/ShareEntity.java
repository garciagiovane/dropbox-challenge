package com.garciagiovane.dropbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareEntity {
    private String fileId;
    private String userToShareWith;
}
