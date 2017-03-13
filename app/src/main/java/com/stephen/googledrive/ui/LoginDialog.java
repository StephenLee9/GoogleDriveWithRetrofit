package com.stephen.googledrive.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stephen.googledrive.R;
import com.stephen.googledrive.utils.CommonUtils;
import com.stephen.googledrive.utils.DriveUtils;

public class LoginDialog extends Dialog {

    private ViewGroup vgLogin;
    private WebView wvLogin;

    private Callback mCallback;

    public LoginDialog(Context context) {
        super(context, R.style.LoginDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
        findView();
        initView();

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                wvLogin.loadUrl(DriveUtils.getAuthUri());
            }
        });
    }

    private void findView() {
        vgLogin = (ViewGroup) findViewById(R.id.vg_login);
        wvLogin = (WebView) findViewById(R.id.wv_login);
    }

    private void initView() {
        vgLogin.setVisibility(View.VISIBLE);
        wvLogin.setVisibility(View.INVISIBLE);
        wvLogin.getSettings().setJavaScriptEnabled(true);
        wvLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                CommonUtils.D("onPageFinished", url);
                wvLogin.setVisibility(View.VISIBLE);
                vgLogin.setVisibility(View.INVISIBLE);

                if (url.contains("error=access_denied") && mCallback != null) {
                    mCallback.onLoginError();
                } else if (url.contains("code=") && mCallback != null) {
                    mCallback.onLoginSuccess(url.replace("http://localhost/?code=", ""));
                }
            }
        });
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onLoginSuccess(String authCode);
        void onLoginError();
    }
}
