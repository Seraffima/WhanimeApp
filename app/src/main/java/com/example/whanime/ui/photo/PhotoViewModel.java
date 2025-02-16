package com.example.whanime.ui.photo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// ViewModel para el fragmento de fotos
public class PhotoViewModel extends ViewModel {

    private final MutableLiveData<String> mText; // LiveData mutable para el texto

    // Constructor que inicializa el LiveData con un valor predeterminado
    public PhotoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    // Método para obtener el LiveData del texto
    public LiveData<String> getText() {
        return mText;
    }
}