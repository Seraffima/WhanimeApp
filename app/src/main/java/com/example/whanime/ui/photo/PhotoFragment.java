package com.example.whanime.ui.photo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import com.example.whanime.R;
import com.example.whanime.api.TraceMoeApi;
import com.example.whanime.api.TraceMoeResponse;
import com.example.whanime.api.ApiClient;
import com.example.whanime.ui.search.SearchItem;
import com.example.whanime.ui.search.SearchViewModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

// Fragmento para manejar la selección y carga de imágenes
public class PhotoFragment extends Fragment {

    private ActivityResultLauncher<Intent> pickImageLauncher; // Lanzador de resultados de actividad para seleccionar imágenes
    private TraceMoeApi traceMoeApi; // API de TraceMoe
    private SearchViewModel searchViewModel; // ViewModel para SearchItem

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        ImageButton buttonSelectImage = view.findViewById(R.id.select_image_button);
        buttonSelectImage.setOnClickListener(v -> selectImageFromGallery());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        uploadImageToApi(selectedImage);
                    }
                }
        );

        traceMoeApi = ApiClient.getClient().create(TraceMoeApi.class);

        // Apply text size preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int textSize = sharedPreferences.getInt("text_size", 16);
        applyTextSize(view, textSize);

        return view;
    }

    // Metodo para seleccionar una imagen de la galería
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // Metodo para cargar una imagen a la API
    private void uploadImageToApi(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            File file = new File(getActivity().getCacheDir(), "upload_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            traceMoeApi.uploadImage(body).enqueue(new Callback<TraceMoeResponse>() {
                @Override
                public void onResponse(Call<TraceMoeResponse> call, Response<TraceMoeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String imageUrl = response.body().result.get(0).image;
                        searchAnimeWithAniList(imageUrl);
                    } else {
                        Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TraceMoeResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "File error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo para buscar anime con AniList usando la URL de la imagen
    private void searchAnimeWithAniList(String imageUrl) {
        traceMoeApi.searchAnimeWithAniList(imageUrl).enqueue(new Callback<TraceMoeResponse>() {
            @Override
            public void onResponse(Call<TraceMoeResponse> call, Response<TraceMoeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TraceMoeResponse.Result result = response.body().result.get(0);
                    if (result.anilist != null && result.anilist.title != null) {
                        String romajiTitle = result.anilist.title.romaji;
                        String videoUrl = result.video;
                        Toast.makeText(getActivity(), "Image uploaded: " + romajiTitle, Toast.LENGTH_SHORT).show();

                        // Crear un nuevo SearchItem
                        SearchItem newItem = new SearchItem(result.image, romajiTitle, result.episode, videoUrl);

                        // Guardar el nuevo SearchItem en la base de datos
                        searchViewModel.insert(newItem);
                    } else {
                        Toast.makeText(getActivity(), "Anime information not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Search failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TraceMoeResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Search error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyTextSize(View view, int textSize) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyTextSize(viewGroup.getChildAt(i), textSize);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }
}