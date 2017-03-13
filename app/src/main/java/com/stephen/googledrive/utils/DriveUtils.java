package com.stephen.googledrive.utils;

import android.util.Log;

import com.stephen.googledrive.Constants;

import java.io.IOException;

import retrofit2.Response;

public final class DriveUtils {

    private DriveUtils() {

    }

    public static String getAuthUri() {
        return String.format(Constants.AUTH_URL, Constants.CLIENT_ID);
    }

    public static String printResponse(String msg, Response<?> response) {
        CommonUtils.D(msg, "onResponse", response.toString());
        if (response.isSuccessful()) {
            CommonUtils.E(msg, "success", response.body());
            return null;
        } else {
            String errorMessage = null;
            try {
                errorMessage = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CommonUtils.W(msg, "failure", errorMessage);
            return msg + " failed: " + errorMessage;
        }
    }

    public static String printFailure(String msg, Throwable t) {
        String message = Log.getStackTraceString(t);
        CommonUtils.W(msg, "onFailure", message);
        return msg + " failed: " + message;
    }
}
