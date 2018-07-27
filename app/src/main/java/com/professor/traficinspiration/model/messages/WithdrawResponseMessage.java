package com.professor.traficinspiration.model.messages;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class WithdrawResponseMessage extends ResponseMessage{


    public String getMsg() {
        return msg;
    }
    @SerializedName("KeyMAC")
    @Expose
    private String keyMAC;
    @SerializedName("KeyMAC_MAC")
    @Expose
    private String keyMACMAC;



    public String getKeyMAC() {
        return keyMAC;
    }

    public void setKeyMAC(String keyMAC) {
        this.keyMAC = keyMAC;
    }

    public String getKeyMACMAC() {
        return keyMACMAC;
    }

    public void setKeyMACMAC(String keyMACMAC) {
        this.keyMACMAC = keyMACMAC;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("msg")
        @Expose
        private String msg;




}
