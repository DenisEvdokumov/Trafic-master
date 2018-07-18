package com.professor.traficinspiration.model.messages;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRequestMessage extends RequestMessage{


    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_MAC")
    @Expose
    private String emailMAC;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("password_MAC")
    @Expose
    private String passwordMAC;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailMAC() {
        return emailMAC;
    }

    public void setEmailMAC(String emailMAC) {
        this.emailMAC = emailMAC;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordMAC() {
        return passwordMAC;
    }

    public void setPasswordMAC(String passwordMAC) {
        this.passwordMAC = passwordMAC;
    }

}
