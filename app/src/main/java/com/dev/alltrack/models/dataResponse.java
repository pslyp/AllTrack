package com.dev.alltrack.models;

public class dataResponse extends defaultResponse {

    private final Data data;

    public dataResponse(int status, String message, Data data) {
        super(status, message);
        this.data = data;
    }

    public Data getData() {
        return data;
    }

}
