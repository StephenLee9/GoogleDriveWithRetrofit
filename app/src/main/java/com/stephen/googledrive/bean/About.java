package com.stephen.googledrive.bean;

import com.google.gson.annotations.SerializedName;

public class About {

    @SerializedName("kind") private String kind;
    @SerializedName("user") private User user;
    @SerializedName("maxUploadSize") private long maxUploadSize;
    @SerializedName("appInstalled") private boolean appInstalled;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public boolean isAppInstalled() {
        return appInstalled;
    }

    public void setAppInstalled(boolean appInstalled) {
        this.appInstalled = appInstalled;
    }

    @Override
    public String toString() {
        return "About <" + "user = " + user + ", maxUploadSize = " + maxUploadSize + '>';
    }
}
