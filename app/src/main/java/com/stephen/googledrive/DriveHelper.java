package com.stephen.googledrive;

import android.util.Log;

import com.stephen.googledrive.bean.About;
import com.stephen.googledrive.bean.DriveFile;
import com.stephen.googledrive.bean.DriveFiles;
import com.stephen.googledrive.bean.Token;
import com.stephen.googledrive.bean.UploadResult;
import com.stephen.googledrive.callback.ListCallback;
import com.stephen.googledrive.callback.StateCallback;
import com.stephen.googledrive.utils.CommonUtils;
import com.stephen.googledrive.utils.DriveUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.stephen.googledrive.Constants.AUTH_TOKEN;
import static com.stephen.googledrive.Constants.BASE_URL_ACCOUNT;
import static com.stephen.googledrive.Constants.BASE_URL_API;
import static com.stephen.googledrive.Constants.CLIENT_ID;
import static com.stephen.googledrive.Constants.CLIENT_SECRET;
import static com.stephen.googledrive.Constants.REDIRECT_URI;
import static com.stephen.googledrive.Constants.ROOT_DRIVE_ID;

public class DriveHelper {

    private String mAuthCode;
    private DriveApi mDriveApi;
    private String mAccessToken;
    private String mRefreshToken;

    public DriveHelper(String authCode) {
        mAuthCode = authCode;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDriveApi = retrofit.create(DriveApi.class);
    }

    /**
     * 获取AccessToken
     * @param callback 回调
     */
    public void getToken(final StateCallback callback) {
        Call<Token> call = mDriveApi.getToken(mAuthCode, CLIENT_ID,
                CLIENT_SECRET, REDIRECT_URI, "authorization_code");
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                String message = DriveUtils.printResponse("getToken", response);
                if (message == null) {
                    Token token = response.body();
                    mAccessToken = token.getAccessToken();
                    mRefreshToken = token.getRefreshToken();
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                String message = DriveUtils.printFailure("getToken", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 刷新AccessToken
     * @param callback 回调
     */
    public void refreshToken(final StateCallback callback) {
        checkRefreshToken();
        Call<Token> call = mDriveApi.refreshToken(mRefreshToken, CLIENT_ID, CLIENT_SECRET, "refresh_token");
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                String message = DriveUtils.printResponse("refreshToken", response);
                if (message == null) {
                    Token token = response.body();
                    mAccessToken = token.getAccessToken();
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                String message = DriveUtils.printFailure("refreshToken", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 注销AccessToken
     * @param callback 回调
     */
    public void revokeToken(final StateCallback callback) {
        checkAccessToken();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_ACCOUNT)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Call<Void> call = retrofit.create(DriveApi.class).revokeToken(mAccessToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String message = DriveUtils.printResponse("revokeToken", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = DriveUtils.printFailure("revokeToken", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 获取用户Google Drive账号信息
     * @param callback 回调
     */
    public void getAbout(final StateCallback callback) {
        Call<About> call = mDriveApi.getAbout(getAuthToken(),
                "kind, user, maxUploadSize, appInstalled");
        call.enqueue(new Callback<About>() {
            @Override
            public void onResponse(Call<About> call, Response<About> response) {
                String message = DriveUtils.printResponse("getAbout", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<About> call, Throwable t) {
                String message = DriveUtils.printFailure("getAbout", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 根据关键字搜索文件列表
     * @param word 搜索关键字
     * @param callback 回调
     */
    public void searchFiles(String word, final ListCallback callback) {
        // 根据关键字搜索文件
        Call<DriveFiles> call = mDriveApi.getFiles(getAuthToken(),
                "modifiedTime", 1000, null, String.format("name contains '%s'", word));
        call.enqueue(new Callback<DriveFiles>() {
            @Override
            public void onResponse(Call<DriveFiles> call, Response<DriveFiles> response) {
                String message = DriveUtils.printResponse("searchFiles", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess(response.body().getFiles());
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriveFiles> call, Throwable t) {
                String message = DriveUtils.printFailure("searchFiles", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 根据父目录ID获取子文件列表
     * @param folderId 父目录ID
     * @param callback 回调
     */
    public void listFiles(String folderId, final ListCallback callback) {
        // 根据父目录的ID搜索文件
        Call<DriveFiles> call = mDriveApi.getFiles(getAuthToken(),
                "name", 1000, null, String.format("'%s' in parents", folderId));
        call.enqueue(new Callback<DriveFiles>() {
            @Override
            public void onResponse(Call<DriveFiles> call, Response<DriveFiles> response) {
                String message = DriveUtils.printResponse("listFiles", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess(response.body().getFiles());
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriveFiles> call, Throwable t) {
                String message = DriveUtils.printFailure("listFiles", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 根据文件ID获取文件信息
     * @param fileId 文件ID
     * @param callback 回调
     */
    public void getFile(String fileId, final StateCallback callback) {
        Call<DriveFile> call = mDriveApi.getFile(getAuthToken(), fileId);
        call.enqueue(new Callback<DriveFile>() {
            @Override
            public void onResponse(Call<DriveFile> call, Response<DriveFile> response) {
                String message = DriveUtils.printResponse("getFile", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriveFile> call, Throwable t) {
                String message = DriveUtils.printFailure("getFile", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 从Google Drive下载文件
     * @param fileId 需要下载的文件ID
     * @param dstFile 下载文件到本地的路径
     * @param callback 回调
     */
    public void downloadFile(String fileId, final File dstFile, final StateCallback callback) {
        Call<ResponseBody> call = mDriveApi.downloadFile(getAuthToken(), fileId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = DriveUtils.printResponse("downloadFile", response);
                if (message == null) {
                    BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(new FileOutputStream(dstFile));
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, length);
                        }
                        bos.flush();
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure(Log.getStackTraceString(e));
                        }
                    } finally {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (bos != null)
                                bos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = DriveUtils.printFailure("downloadFile", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    public void uploadFile(File srcFile, String folderId, StateCallback callback) {
        uploadFile(srcFile, srcFile.getName(), folderId, callback);
    }

    /**
     * 上传文件到指定目录
     * @param srcFile 本地需要上传的文件
     * @param title 上传文件名称
     * @param folderId 上传文件父目录ID
     * @param callback 回调
     */
    public void uploadFile(File srcFile, String title, final String folderId, final StateCallback callback) {
        MediaType contentType = MediaType.parse("application/json; charset=UTF-8");
        String content = "{\"name\": \"" + title + "\"}";
        MultipartBody.Part metaPart = MultipartBody.Part.create(RequestBody.create(contentType, content));
        String mimeType = CommonUtils.getMimeType(srcFile);
        MultipartBody.Part dataPart = MultipartBody.Part.create(RequestBody.create(MediaType.parse(mimeType), srcFile));

        Call<UploadResult> call = mDriveApi.uploadFile(getAuthToken(), metaPart, dataPart);
        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                String message = DriveUtils.printResponse("uploadFile", response);
                if (message == null) {
                    moveFile(response.body().getId(), folderId, new StateCallback() {
                        @Override
                        public void onSuccess() {
                            if (callback != null) {
                                callback.onSuccess();
                            }
                        }

                        @Override
                        public void onFailure(String msg) {
                            if (callback != null) {
                                callback.onFailure(msg);
                            }
                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                String message = DriveUtils.printFailure("uploadFile", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 从根目录移动文件
     * @param fileId 需要移动的文件ID
     * @param dstFolderId 目标目录ID
     * @param callback 回调
     */
    public void moveFile(String fileId, String dstFolderId, StateCallback callback) {
        moveFile(fileId, dstFolderId, ROOT_DRIVE_ID, callback);
    }

    /**
     * 移动文件
     * @param fileId 需要移动的文件ID
     * @param dstFolderId 目标目录ID
     * @param srcFolderId 源目录ID
     * @param callback 回调
     */
    public void moveFile(String fileId, String dstFolderId, String srcFolderId, final StateCallback callback) {
        Call<DriveFile> call = mDriveApi.moveFile(getAuthToken(), fileId, dstFolderId, srcFolderId);
        call.enqueue(new Callback<DriveFile>() {
            @Override
            public void onResponse(Call<DriveFile> call, Response<DriveFile> response) {
                String message = DriveUtils.printResponse("moveFile", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<DriveFile> call, Throwable t) {
                String message = DriveUtils.printFailure("moveFile", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    /**
     * 删除文件
     * @param fileId 需要删除的文件ID
     * @param callback 回调
     */
    public void deleteFile(String fileId, final StateCallback callback) {
        Call<Void> call = mDriveApi.deleteFile(getAuthToken(), fileId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String message = DriveUtils.printResponse("deleteFile", response);
                if (message == null) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String message = DriveUtils.printFailure("deleteFile", t);
                if (callback != null) {
                    callback.onFailure(message);
                }
            }
        });
    }

    private String getAuthToken() {
        checkAccessToken();
        return String.format(AUTH_TOKEN, mAccessToken);
    }

    private void checkRefreshToken() {
        if (mRefreshToken == null) {
            throw new IllegalStateException("Refresh token is null!");
        }
    }

    private void checkAccessToken() {
        if (mAccessToken == null) {
            throw new IllegalStateException("Access token is null!");
        }
    }
}
