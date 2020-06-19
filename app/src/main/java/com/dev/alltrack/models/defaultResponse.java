package com.dev.alltrack.models;

public class defaultResponse {

    private final int status;
    private final String message;

    public defaultResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
