package com.garciagiovane.dropbox.contract.v1.user.mapper;

import com.garciagiovane.dropbox.contract.v1.user.model.request.ShareRequest;
import com.garciagiovane.dropbox.impl.v1.user.model.ShareModel;

public class ShareMapper {
    public static ShareModel mapToImpl(ShareRequest request) {
        return ShareModel.builder()
                .fileId(request.getFileId())
                .userRecipientId(request.getUserRecipientId())
                .build();
    }
}
