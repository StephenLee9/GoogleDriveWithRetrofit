package com.stephen.googledrive.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class DriveFiles {

    @SerializedName("kind") private String kind;
    @SerializedName("nextPageToken") private String nextPageToken;
    @SerializedName("files") private DriveFile[] files;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public DriveFile[] getFiles() {
        return files;
    }

    public void setFiles(DriveFile[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "DriveFiles <" + "nextPageToken = " + nextPageToken +
                ", files = " + Arrays.toString(files) + '>';
    }
}
