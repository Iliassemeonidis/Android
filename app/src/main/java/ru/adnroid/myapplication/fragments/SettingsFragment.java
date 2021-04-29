package ru.adnroid.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ru.adnroid.myapplication.R;

public class SettingsFragment extends Fragment {

    public static final String APP_PREFERENCES_KEY = "MY_SETTINGS";
    public static final String APP_PREFERENCES_THEME = "THEME";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwitch(view);
    }

    private void initSwitch(@NonNull View view) {
        SharedPreferences mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        SwitchCompat theme = view.findViewById(R.id.theme_switch);
        theme.setChecked(isBlackThem());

        theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            editor.putBoolean(APP_PREFERENCES_THEME, isChecked);
            editor.apply();
        });
    }

    private boolean isBlackThem() {
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(APP_PREFERENCES_THEME, false);
    }
}
