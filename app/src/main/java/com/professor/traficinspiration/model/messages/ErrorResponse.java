package com.professor.traficinspiration.model.messages;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("message")
    private String keysConfirm;

    public String getKeysConfirm() {
        return keysConfirm;
    }

    public void setKeysConfirm(String keysConfirm) {
        this.keysConfirm = keysConfirm;
    }
}
