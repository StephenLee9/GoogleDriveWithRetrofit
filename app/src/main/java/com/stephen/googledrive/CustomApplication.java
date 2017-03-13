package com.stephen.googledrive;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

public class CustomApplication extends Application {

    private static CustomApplication mInstance;
    private static Handler mHandler = new Handler();
    private static Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static void toast(int resId) {
        if (mInstance != null) {
            toast(mInstance.getString(resId));
        }
    }

    public static void toast(int resId, Object... formatArgs) {
        if (mInstance != null) {
            toast(mInstance.getString(resId, formatArgs));
        }
    }

    public static void toast(final String text) {
        if (mInstance == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mInstance, text, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(text);
                }
                mToast.show();
            }
        });
    }
}
