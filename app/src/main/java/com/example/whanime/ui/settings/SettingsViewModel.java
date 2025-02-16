package com.example.whanime.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// ViewModel para manejar la configuración
public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText; // LiveData mutable para el texto

    // Constructor que inicializa el LiveData con un valor predeterminado
    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    // Metodo para obtener el texto
    public LiveData<String> getText() {
        return mText;
    }
}