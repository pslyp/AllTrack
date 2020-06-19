package com.dev.alltrack.models;

public class Status {

    private final String code;
    private final String date;
    private final String detail;
    private final String province;

    public Status(String code, String date, String detail, String province) {
        this.code = code;
        this.date = date;
        this.detail = detail;
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getDetail() {
        return detail;
    }

    public String getProvince() {
        return province;
    }

}
