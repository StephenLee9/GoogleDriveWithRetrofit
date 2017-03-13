package com.stephen.googledrive.bean;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("access_token") private String accessToken;
    @SerializedName("expires_in") private long expiresIn;
    @SerializedName("token_type") private String tokenType;
    @SerializedName("refresh_token") private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Token <" + "accessToken = " + accessToken +
                ", tokenType = " + tokenType + ", refreshToken = " + refreshToken + '>';
    }
}
