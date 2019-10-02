package com.garciagiovane.dropbox.stubs;

import com.garciagiovane.dropbox.impl.v1.user.model.Viewer;

public class ViewerStub {
    public static Viewer getViewer() {
        return Viewer.builder()
                .id("someId")
                .email("teste@teste.com")
                .name("teste")
                .build();
    }
}
