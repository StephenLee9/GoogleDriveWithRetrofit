package com.stephen.googledrive.callback;

public interface StateCallback {
    void onSuccess();
    void onFailure(String msg);
}
