package com.garciagiovane.dropbox.impl.v1.user.stubs;

import com.garciagiovane.dropbox.impl.v1.user.model.ShareModel;

public class ShareModelStub {
    public static ShareModel getShareModel() {
        return ShareModel.builder()
                .userRecipientId("recipientId")
                .fileId("fileId")
                .build();
    }
}
