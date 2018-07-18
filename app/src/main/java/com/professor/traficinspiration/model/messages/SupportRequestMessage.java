package com.professor.traficinspiration.model.messages;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SupportRequestMessage extends RequestMessage{



    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("msg_MAC")
    @Expose
    private String msgMAC;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgMAC() {
        return msgMAC;
    }

    public void setMsgMAC(String msgMAC) {
        this.msgMAC = msgMAC;
    }
}
