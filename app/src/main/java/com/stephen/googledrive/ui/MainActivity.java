package com.stephen.googledrive.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.stephen.googledrive.Constants;
import com.stephen.googledrive.CustomApplication;
import com.stephen.googledrive.DriveHelper;
import com.stephen.googledrive.R;
import com.stephen.googledrive.callback.StateCallback;
import com.stephen.googledrive.utils.CommonUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TEST_FILE_ID = "0BzHAMvdMNu8aZjVtTUVBSVF6bE0";
    public static final String TEST_FOLDER_ID = "0BzHAMvdMNu8aSFNBVFdhWldFM2c";

    private LoginDialog mLoginDialog;
    private DriveHelper mDriveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main_login).setOnClickListener(this);
        findViewById(R.id.btn_main_get_token).setOnClickListener(this);
        findViewById(R.id.btn_main_refresh_token).setOnClickListener(this);
        findViewById(R.id.btn_main_revoke_token).setOnClickListener(this);
        findViewById(R.id.btn_main_get_about).setOnClickListener(this);
        findViewById(R.id.btn_main_search_files).setOnClickListener(this);
        findViewById(R.id.btn_main_list_files).setOnClickListener(this);
        findViewById(R.id.btn_main_get_file).setOnClickListener(this);
        findViewById(R.id.btn_main_download_file).setOnClickListener(this);
        findViewById(R.id.btn_main_upload_file).setOnClickListener(this);
        findViewById(R.id.btn_main_move_file).setOnClickListener(this);
        findViewById(R.id.btn_main_delete_file).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                showLoginDialog(Constants.DIALOG_SHOW);
                break;
            case R.id.btn_main_get_token:
                if (mDriveHelper != null) {
                    mDriveHelper.getToken(null);
                }
                break;
            case R.id.btn_main_refresh_token:
                if (mDriveHelper != null) {
                    mDriveHelper.refreshToken(null);
                }
                break;
            case R.id.btn_main_revoke_token:
                if (mDriveHelper != null) {
                    mDriveHelper.revokeToken(new StateCallback() {
                        @Override
                        public void onSuccess() {
                            mDriveHelper = null;
                        }

                        @Override
                        public void onFailure(String msg) {

                        }
                    });
                }
                break;
            case R.id.btn_main_get_about:
                if (mDriveHelper != null) {
                    mDriveHelper.getAbout(null);
                }
                break;
            case R.id.btn_main_search_files:
                if (mDriveHelper != null) {
                    mDriveHelper.searchFiles(".", null);
                }
                break;
            case R.id.btn_main_list_files:
                if (mDriveHelper != null) {
                    mDriveHelper.listFiles(Constants.ROOT_DRIVE_ID, null);
                }
                break;
            case R.id.btn_main_get_file:
                if (mDriveHelper != null) {
                    mDriveHelper.getFile(Constants.ROOT_DRIVE_ID, null);
                }
                break;
            case R.id.btn_main_download_file:
                if (mDriveHelper != null) {
                    mDriveHelper.downloadFile(TEST_FILE_ID, new File(Constants.SDCARD_PATH + "/test.pdf"), null);
                }
                break;
            case R.id.btn_main_upload_file:
                if (mDriveHelper != null) {
                    mDriveHelper.uploadFile(new File(Constants.SDCARD_PATH + "/test.pdf"), TEST_FOLDER_ID, null);
                }
                break;
            case R.id.btn_main_move_file:
                if (mDriveHelper != null) {
                    mDriveHelper.moveFile(TEST_FILE_ID, TEST_FOLDER_ID, null);
                }
                break;
            case R.id.btn_main_delete_file:
                if (mDriveHelper != null) {
                    mDriveHelper.deleteFile(TEST_FILE_ID, null);
                }
                break;
        }
    }

    private void showLoginDialog(int mode) {
        if (mLoginDialog == null) {
            mLoginDialog = new LoginDialog(this);
            mLoginDialog.setCallback(new LoginDialog.Callback() {
                @Override
                public void onLoginSuccess(String authCode) {
                    loginSuccess(authCode);
                }

                @Override
                public void onLoginError() {
                    loginError();
                }
            });
        }
        switch (mode) {
            case Constants.DIALOG_SHOW:
                if (!mLoginDialog.isShowing()) {
                    mLoginDialog.show();
                }
                break;
            case Constants.DIALOG_CANCEL:
                if (mLoginDialog.isShowing()) {
                    mLoginDialog.cancel();
                }
                break;
            case Constants.DIALOG_SWITCH:
                if (!mLoginDialog.isShowing()) {
                    mLoginDialog.show();
                } else {
                    mLoginDialog.cancel();
                }
                break;
        }
    }

    private void loginSuccess(String authCode) {
        showLoginDialog(Constants.DIALOG_CANCEL);
        CommonUtils.D("loginSuccess", authCode);
        CustomApplication.toast(R.string.login_success);
        mDriveHelper = new DriveHelper(authCode);
    }

    private void loginError() {
        showLoginDialog(Constants.DIALOG_CANCEL);
        CommonUtils.D("loginError");
        CustomApplication.toast(R.string.login_error);
    }
}
