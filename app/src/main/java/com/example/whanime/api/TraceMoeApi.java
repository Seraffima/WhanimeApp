package com.example.whanime.api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

// Interfaz para definir las llamadas a la API de TraceMoe
public interface TraceMoeApi {

    // Metodo para subir una imagen utilizando una solicitud POST multipart
    @Multipart
    @POST("/search")
    Call<TraceMoeResponse> uploadImage(@Part MultipartBody.Part image);

    // Metodo para buscar anime utilizando una URL de imagen con una solicitud GET
    @GET("/search")
    Call<TraceMoeResponse> searchAnime(@Query("url") String imageUrl);

    // Metodo para buscar anime con información de AniList utilizando una URL de imagen con una solicitud GET
    @GET("/search?anilistInfo")
    Call<TraceMoeResponse> searchAnimeWithAniList(@Query("url") String imageUrl);
}