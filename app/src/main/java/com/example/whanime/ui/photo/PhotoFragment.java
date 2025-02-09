package com.example.whanime.ui.photo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.whanime.R;

public class PhotoFragment extends Fragment {

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button buttonPasteImage = view.findViewById(R.id.button_paste_image);
        Button buttonSelectImage = view.findViewById(R.id.button_select_image);

        buttonPasteImage.setOnClickListener(v -> pasteImageFromClipboard());
        buttonSelectImage.setOnClickListener(v -> selectImageFromGallery());

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        // Handle the selected image URI (e.g., display it in an ImageView)
                        Toast.makeText(getActivity(), "Image selected: " + selectedImage, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        return view;
    }

    private void pasteImageFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType("image/*")) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            Uri imageUri = item.getUri();
            // Handle the image URI (e.g., display it in an ImageView)
            Toast.makeText(getActivity(), "Image pasted from clipboard: " + imageUri, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No image in clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }
}