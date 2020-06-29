package com.dev.alltrack.models;

public class Package {

    private final String status;
    private final String code;
    private final String company;
    private final String description;

    public Package(String status, String no, String company, String description) {
        this.status = status;
        this.code = no;
        this.company = company;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

}
