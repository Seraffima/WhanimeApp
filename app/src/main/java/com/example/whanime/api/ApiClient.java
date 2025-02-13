package com.example.whanime.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.trace.moe";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(String.class, new EpisodeDeserializer())
                    .registerTypeAdapter(TraceMoeResponse.Result.Anilist.class, new AnilistDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}