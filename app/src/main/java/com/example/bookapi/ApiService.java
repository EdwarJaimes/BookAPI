package com.example.bookapi;

import com.example.bookapi.model.ApiKey;
import com.example.bookapi.model.DropAllSessions;
import com.example.bookapi.model.GetBooksResponse;
import com.example.bookapi.model.OauthKeyResponse;
import com.example.bookapi.model.SesskeyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/live/api.php?req=createAppkey&appname=BookAPI")
    Call<ApiKey> createAppKey();

    @GET("/live/api.php?req=createOauthkey")
    Call<OauthKeyResponse> createOauthKey(@Query("login") String user, @Query("pwd") String pwd, @Query("appkey") String appkey);

    @GET("/live/api.php?req=createSesskey")
    Call<SesskeyResponse> createSessKey(@Query("o_u") String o_u, @Query("oauthkey") String oauthkey, @Query("restrictions") String restrictions);

    @GET("/live/api.php?req=getAllBooks")
    Call<GetBooksResponse> getAllBooks(@Query("u_c") String o_u2, @Query("o_u") String o_u, @Query("sesskey") String sesskey);

    @GET("/live/api.php?req=dropAllSessions")
    Call<DropAllSessions> dropAllSessions(@Query("o_u") String o_u, @Query("sesskey") String sesskey);

}
