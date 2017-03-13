package com.stephen.googledrive;

import android.os.Environment;

public interface Constants {

    public static final int DIALOG_SHOW = 0x1;
    public static final int DIALOG_CANCEL = 0x2;
    public static final int DIALOG_SWITCH = 0x3;

    /** OAuth：https://developers.google.com/identity/protocols/OpenIDConnect */
    public static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth?" +
            "client_id=%s&" +
            "response_type=code&" +
            "scope=https://www.googleapis.com/auth/drive+https://www.googleapis.com/auth/userinfo.profile&" +
            "redirect_uri=http://localhost/";
    public static final String BASE_URL_API = "https://www.googleapis.com";
    public static final String BASE_URL_ACCOUNT = "https://accounts.google.com";
    /** 创建凭据：https://console.developers.google.com/apis/credentials */
    public static final String CLIENT_ID = "[CLIENT-ID]";
    public static final String CLIENT_SECRET = "[CLIENT-SECRET]";
    public static final String REDIRECT_URI = "http://localhost/";
    public static final String AUTH_TOKEN = "Bearer %s";
    public static final String ROOT_DRIVE_ID = "root";

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
}
