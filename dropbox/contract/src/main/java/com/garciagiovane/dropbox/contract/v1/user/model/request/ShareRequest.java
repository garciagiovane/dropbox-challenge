package com.garciagiovane.dropbox.contract.v1.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequest {
    private String userRecipientId;
    private String fileId;
}
