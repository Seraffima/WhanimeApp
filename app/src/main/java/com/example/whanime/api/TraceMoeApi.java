package com.example.whanime.api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
public interface TraceMoeApi {
    @Multipart
    @POST("/search")
    Call<TraceMoeResponse> uploadImage(@Part MultipartBody.Part image);
    @GET("/search")
    Call<TraceMoeResponse> searchAnime(@Query("url") String imageUrl);
}
