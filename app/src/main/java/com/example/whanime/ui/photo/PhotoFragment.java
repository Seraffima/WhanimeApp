package com.example.whanime.ui.photo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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

public class PhotoFragment extends Fragment {

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private TraceMoeApi traceMoeApi;
    private SearchViewModel searchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        Button buttonSelectImage = view.findViewById(R.id.button_select_image);
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

        return view;
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

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
                        TraceMoeResponse.Result result = response.body().result.get(0);
                        Toast.makeText(getActivity(), "Image uploaded: " + result.filename, Toast.LENGTH_SHORT).show();

                        // Create a new SearchItem
                        SearchItem newItem = new SearchItem(result.image, result.filename, result.episode);

                        // Save the new SearchItem to the database
                        searchViewModel.insert(newItem);
                    } else {
                        Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TraceMoeResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", t.getMessage(), t);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "File error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}