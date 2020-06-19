package com.dev.alltrack.models;

import java.util.List;

public class Data {

    private final Info info;
    private final List<Status> status;

    public Data(Info info, List<Status> status) {
        this.info = info;
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public List<Status> getStatus() {
        return status;
    }

}
