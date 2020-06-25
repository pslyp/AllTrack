package com.dev.alltrack.models;

import com.google.gson.annotations.SerializedName;

public class Info {

    private final String no;
    private final String sender;
    private final String receiver;

    public Info(String no, String sender, String receiver, String lsd) {
        this.no = no;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getNo() {
        return no;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

}
