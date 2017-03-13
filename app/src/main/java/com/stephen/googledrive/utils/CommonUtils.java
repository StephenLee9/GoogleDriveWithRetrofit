package com.stephen.googledrive.utils;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Arrays;

public final class CommonUtils {

    private static final String TAG = "StephenLee";
    private static final String DIVIDER = " - ";
    private static boolean isDebug = true;

    private CommonUtils() {

    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static String getExtension(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (!lowerName.contains("."))
            return "";
        return lowerName.substring(lowerName.lastIndexOf(".") + 1);
    }

    public static String getMimeType(File file) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String mimeType = map.getMimeTypeFromExtension(getExtension(file));
        if (TextUtils.isEmpty(mimeType))
            return "*/*";
        return mimeType;
    }

    public static void setDebug(boolean isDebug) {
        CommonUtils.isDebug = isDebug;
    }

    public static void V(Object... args) {
        println(Log.VERBOSE, args);
    }

    public static void D(Object... args) {
        println(Log.DEBUG, args);
    }

    public static void I(Object... args) {
        println(Log.INFO, args);
    }

    public static void W(Object... args) {
        println(Log.WARN, args);
    }

    public static void E(Object... args) {
        println(Log.ERROR, args);
    }

    private static void println(int priority, Object... args) {
        if (args == null || args.length == 0) {
            println(priority, Arrays.toString(args));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(args[0]);
        for (int i = 1; i < args.length; i++) {
            sb.append(DIVIDER).append(args[i]);
        }
        println(priority, sb.toString());
    }

    private static void println(int priority, String msg) {
        if (isDebug) {
            Log.println(priority, TAG, msg);
        }
    }
}
