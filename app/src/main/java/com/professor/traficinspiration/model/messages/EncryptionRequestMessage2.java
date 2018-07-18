package com.professor.traficinspiration.model.messages;

import com.google.gson.annotations.SerializedName;

public class EncryptionRequestMessage2 extends RequestMessage{


    @SerializedName("string")
    private String string;

    @SerializedName("keysConfirm")
    private Integer keysConfirm;

    @SerializedName("string_MAC")
    private String stringMAC;


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Integer getKeysConfirm() {
        return keysConfirm;
    }

    public void setKeysConfirm(Integer keysConfirm) {
        this.keysConfirm = keysConfirm;
    }


    public void setStringMAC(String stringMAC) {
        this.stringMAC = stringMAC;
    }
}
