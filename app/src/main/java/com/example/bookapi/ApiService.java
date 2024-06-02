package com.example.bookapi;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/live/api.php?req=createAppkey&appname=BookAPI")
    Call<Post> createAppKey();

    @GET("/live/api.php?req=createOauthkey")
    Call<OauthKeyResponse> createOauthKey(@Query("login") String user, @Query("pwd") String pwd, @Query("appkey") String appkey);

    @GET("/live/api.php?req=createSesskey")
    Call<SesskeyResponse> createSessKey(@Query("o_u") String o_u, @Query("oauthkey") String oauthkey, @Query("restrictions") String restrictions);


    //   https://timetonic.com/live/api.php?req=createSesskey&o_u=androiddeveloper&oauthkey=Y8xA-5PEq-RSIZ-J1yb-PWmG-5lH4-QhjI&restrictions=%3D
    //   https://timetonic.com/live/api.php?req=getAllBooks&o_u=androiddeveloper&oauthkey=FRS9-JeiZ-a1y6-1iA7-5uvt-rjel-j5KF&sskey=PFNF-m1bP-UpDL-6wuL-X8il-lG84-lJ12
    //                                      req=getAllBooks&u_c=zo              &o_u=zo                                     &sesskey=gm7q-NqRH-aByq-e6Kh-DwGQ-0

    //   https://timetonic.com/live/api.php?req=getAllBooks&u_c=androiddeveloper&o_u=Vm2b-kMv3-R5Mf-d9zV-qRpY-dG3v-28T2&sskey=QqQP-mG1h-EUAD-HDsx-sWtc-lbYs-x5w9
    //   https://timetonic.com/live/api.php?req=getAllBooks&u_c=androiddeveloper&o_u=MWEk-FGYY-jmhW-8FX2-tkTb-sATw-bRm2&sesskey=8mQP-yduE-n9IF-1Ehr-YRlk-xuxl-UQKL
    //   https://timetonic.com/live/api.php?req=getAllBooks&u_c=Rzm2-7Qpj-fqz4-xQUI-IT2i-SByS-KADr&o_u=androiddeveloper&sesskey=k76f-G36V-16eF-Bzvx-SxnC-5LU9-ZdtN (269ms)
    @GET("/live/api.php?req=getAllBooks")
    Call<GetBooksResponse> getAllBooks(@Query("u_c") String o_u2, @Query("o_u") String o_u, @Query("sesskey") String sesskey);

}
