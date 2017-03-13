package com.stephen.googledrive;

import com.stephen.googledrive.bean.About;
import com.stephen.googledrive.bean.DriveFile;
import com.stephen.googledrive.bean.DriveFiles;
import com.stephen.googledrive.bean.Token;
import com.stephen.googledrive.bean.UploadResult;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriveApi {

    /**
     * https://developers.google.com/identity/protocols/OAuth2InstalledApp
     */
    @POST("/oauth2/v4/token")
    @FormUrlEncoded
    Call<Token> getToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType
    );

    /**
     * https://developers.google.com/identity/protocols/OAuth2InstalledApp
     */
    @POST("/oauth2/v4/token")
    @FormUrlEncoded
    Call<Token> refreshToken(
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType
    );

    /**
     * https://accounts.google.com/o/oauth2/revoke?token={token}
     */
    @GET("/o/oauth2/revoke")
    Call<Void> revokeToken(
            @Query("token") String token
    );

    /**
     * https://developers.google.com/drive/v3/reference/about
     */
    @GET("/drive/v3/about")
    Call<About> getAbout(
            @Header("Authorization") String authToken,
            @Query("fields") String fields
    );

    /**
     * https://developers.google.com/drive/v3/reference/files/list
     */
    @GET("/drive/v3/files")
    Call<DriveFiles> getFiles(
            @Header("Authorization") String authToken,
            @Query("orderBy") String orderBy,
            @Query("pageSize") int pageSize,
            @Query("pageToken") String pageToken,
            @Query("q") String q
    );

    /**
     * https://developers.google.com/drive/v3/reference/files/get
     */
    @GET("/drive/v3/files/fileId")
    Call<DriveFile> getFile(
            @Header("Authorization") String authToken,
            @Query("fileId") String driveId
    );

    /**
     * https://developers.google.com/drive/v3/web/manage-downloads
     */
    @GET("/drive/v3/files/{fileId}?alt=media")
    Call<ResponseBody> downloadFile(
            @Header("Authorization") String authToken,
            @Path(value = "fileId") String fileId
    );

    /**
     * https://developers.google.com/drive/v3/web/multipart-upload
     */
    @POST("/upload/drive/v3/files?uploadType=multipart")
    @Multipart
    Call<UploadResult> uploadFile(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part metaPart,
            @Part MultipartBody.Part dataPart
    );

    /**
     * https://developers.google.com/drive/v3/reference/files/update
     */
    @PATCH("/drive/v3/files/{fileId}")
    Call<DriveFile> moveFile(
            @Header("Authorization") String authToken,
            @Path(value = "fileId") String fileId,
            @Query("addParents") String addParents,
            @Query("removeParents") String removeParents
    );

    /**
     * https://developers.google.com/drive/v3/reference/files/delete
     */
    @DELETE("/drive/v3/files/{fileId}")
    Call<Void> deleteFile(
            @Header("Authorization") String authToken,
            @Path(value = "fileId") String fileId
    );
}
