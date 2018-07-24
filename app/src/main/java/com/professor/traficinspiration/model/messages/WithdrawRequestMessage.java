package com.professor.traficinspiration.model.messages;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawRequestMessage extends RequestMessage{

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone_MAC")
    @Expose
    private String phoneMAC;
    @SerializedName("acc")
    @Expose
    private String acc;
    @SerializedName("acc_MAC")
    @Expose
    private String accMAC;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("msg_MAC")
    @Expose
    private String msgMAC;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("type_MAC")
    @Expose
    private String typeMAC;
    @SerializedName("sum")
    @Expose
    private String sum;
    @SerializedName("sum_MAC")
    @Expose
    private String sumMAC;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneMAC() {
        return phoneMAC;
    }

    public void setPhoneMAC(String phoneMAC) {
        this.phoneMAC = phoneMAC;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getAccMAC() {
        return accMAC;
    }

    public void setAccMAC(String accMAC) {
        this.accMAC = accMAC;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeMAC() {
        return typeMAC;
    }

    public void setTypeMAC(String typeMAC) {
        this.typeMAC = typeMAC;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getSumMAC() {
        return sumMAC;
    }

    public void setSumMAC(String sumMAC) {
        this.sumMAC = sumMAC;
    }
}
