package ru.adnroid.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class EditFragment extends Fragment {
    private static final String ARG_PARAM1 = "ARG_PARAM1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextTitle = view.findViewById(R.id.title_edit_text);
        EditText editTextDescription = view.findViewById(R.id.description_edit_text);
        Button buttonSave = view.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(v -> {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
               fragmentActivity.getSupportFragmentManager().popBackStack();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}