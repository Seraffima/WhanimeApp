package com.example.whanime.ui.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.example.whanime.R;

import java.util.Locale;

// Fragmento para manejar la configuración de la aplicación
public class SettingsFragment extends Fragment {

    private boolean isLocaleSet = false; // Bandera para evitar múltiples reinicios de la actividad

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Switch para el modo oscuro
        Switch switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkModeEnabled);

        // Listener para el cambio de estado del switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            // Cambiar el modo de la aplicación
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // SeekBar para el tamaño del texto
        SeekBar seekBarTextSize = view.findViewById(R.id.seekbar_text_size);
        int textSize = sharedPreferences.getInt("text_size", 16);
        seekBarTextSize.setProgress(textSize);

        seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("text_size", progress);
                editor.apply();
                updateTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No hacer nada
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No hacer nada
            }
        });

        // Spinner para la selección de idioma
        Spinner spinnerLanguage = view.findViewById(R.id.spinner_language);
        String currentLanguage = sharedPreferences.getString("language", "en");
        setSpinnerSelection(spinnerLanguage, currentLanguage);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = getLanguageCode(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();
                setLocale(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Aplicar la preferencia de tamaño de texto
        applyTextSize(view, textSize);

        return view;
    }

    private void updateTextSize(int textSize) {
        // Actualizar el tamaño del texto de las vistas relevantes
        View rootView = getActivity().findViewById(android.R.id.content);
        if (rootView != null) {
            updateTextSizeRecursively(rootView, textSize);
        }
    }

    private void updateTextSizeRecursively(View view, int textSize) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                updateTextSizeRecursively(viewGroup.getChildAt(i), textSize);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
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

    private void setSpinnerSelection(Spinner spinner, String languageCode) {
        String[] languages = getResources().getStringArray(R.array.language_options);
        for (int i = 0; i < languages.length; i++) {
            if (getLanguageCode(i).equals(languageCode)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private String getLanguageCode(int position) {
        switch (position) {
            case 0:
                return "en";
            case 1:
                return "es";
            // Agregar más casos para idiomas adicionales
            default:
                return "en";
        }
    }

    private void setLocale(String languageCode) {
        Locale newLocale = new Locale(languageCode);
        Locale currentLocale = getResources().getConfiguration().locale;

        if (!newLocale.equals(currentLocale)) {
            Locale.setDefault(newLocale);
            Configuration config = getResources().getConfiguration();
            config.setLocale(newLocale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            // Restart the activity to apply the new locale
            getActivity().recreate();
        }
    }
}