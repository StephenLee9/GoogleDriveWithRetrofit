package com.stephen.googledrive.callback;

import com.stephen.googledrive.bean.DriveFile;

public interface ListCallback {
    void onSuccess(DriveFile[] driveFiles);
    void onFailure(String msg);
}
