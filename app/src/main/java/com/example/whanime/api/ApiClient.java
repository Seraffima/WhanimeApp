package com.example.whanime.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Clase ApiClient para gestionar la instancia de Retrofit
public class ApiClient {
    private static final String BASE_URL = "https://api.trace.moe";
    private static Retrofit retrofit = null;

    // Metodo para obtener la instancia de Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configuración de Gson con deserializadores personalizados
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(String.class, new EpisodeDeserializer())
                    .registerTypeAdapter(TraceMoeResponse.Result.Anilist.class, new AnilistDeserializer())
                    .create();

            // Construcción de la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}