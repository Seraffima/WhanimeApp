package com.example.whanime.ui.search;

import android.util.Log;

import com.example.whanime.api.ApiClient;
import com.example.whanime.api.TraceMoeApi;
import com.example.whanime.api.TraceMoeResponse;
import com.example.whanime.dbHandling.DatabaseOperations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Repositorio para manejar la obtención y almacenamiento de elementos de búsqueda
public class SearchItemRepository {
    private DatabaseOperations databaseOperations; // Operaciones de base de datos
    private TraceMoeApi traceMoeApi; // API de TraceMoe

    // Constructor que inicializa las operaciones de base de datos y la API
    public SearchItemRepository(DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
        this.traceMoeApi = ApiClient.getClient().create(TraceMoeApi.class);
    }

    // Metodo para obtener y guardar un elemento de búsqueda
    public void fetchAndSaveSearchItem(String imageUrl) {
        traceMoeApi.searchAnime(imageUrl).enqueue(new Callback<TraceMoeResponse>() {
            @Override
            public void onResponse(Call<TraceMoeResponse> call, Response<TraceMoeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TraceMoeResponse.Result result = response.body().result.get(0);
                    SearchItem searchItem = new SearchItem(result.image, result.anilist.title.romaji, "Episode: " + result.episode, result.video);
                    databaseOperations.insertSearchItem(searchItem);
                } else {
                    Log.e("API_ERROR", "Respuesta no exitosa o vacía");
                }
            }

            @Override
            public void onFailure(Call<TraceMoeResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error al obtener datos", t);
            }
        });
    }
}