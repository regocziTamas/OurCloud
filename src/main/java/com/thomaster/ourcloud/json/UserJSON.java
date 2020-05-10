package com.thomaster.ourcloud.json;

import com.thomaster.ourcloud.model.user.OCUser;

public class UserJSON {

    private String username;
    private long usedBytes;

    public UserJSON(OCUser source) {
        this.username = source.getUsername();
        this.usedBytes = source.getUsedBytes();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUsedBytes() {
        return usedBytes;
    }

    public void setUsedBytes(long usedBytes) {
        this.usedBytes = usedBytes;
    }
}
