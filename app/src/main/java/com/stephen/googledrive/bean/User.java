package com.stephen.googledrive.bean;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("kind") private String kind;
    @SerializedName("displayName") private String displayName;
    @SerializedName("photoLink") private String photoLink;
    @SerializedName("me") private boolean me;
    @SerializedName("permissionId") private String permissionId;
    @SerializedName("emailAddress") private String emailAddress;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User <" + "displayName = " + displayName + ", emailAddress = " + emailAddress + '>';
    }
}
