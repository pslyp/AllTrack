package com.dev.alltrack.models;

import com.google.gson.annotations.SerializedName;

public class Info {

    private final String no;
    private final String company;
    private final String sender;
    private final String receiver;

    public Info(String no, String company, String sender, String receiver) {
        this.no = no;
        this.company = company;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getNo() {
        return no;
    }

    public String getCompany() {
        return company;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

}
