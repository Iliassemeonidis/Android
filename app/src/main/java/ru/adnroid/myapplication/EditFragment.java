package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Objects;

import static ru.adnroid.myapplication.DetailsFragment.EXTRA_PARAMS;
import static ru.adnroid.myapplication.DetailsFragment.REQUEST_CODE;

public class EditFragment extends Fragment {
    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private int color;

    public static EditFragment newInstance(NoteBundle param1) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] colors = getResources().getStringArray(R.array.colors);
        NoteBundle noteParams = Objects.requireNonNull(getArguments()).getParcelable(ARG_PARAM1);
        EditText editTextTitle = view.findViewById(R.id.title_edit_text);
        editTextTitle.setText(noteParams.getTitle());
        EditText editTextDescription = view.findViewById(R.id.description_edit_text);
        editTextDescription.setText(noteParams.getDescription());
        // Попросить поговорить про адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerColors = view.findViewById(R.id.spinner_colors);
        spinnerColors.setAdapter(adapter);
        spinnerColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        color = Color.BLACK;
                        break;
                    case 1:
                        color = Color.WHITE;
                        break;
                    case 2:
                        color = Color.BLUE;
                        break;
                    case 3:
                        color = Color.RED;
                        break;
                    case 4:
                        color = Color.GREEN;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buttonSave = view.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(v -> {
            // попросить для особо одаренных еще раз пройтись по созданию фрашмента по каждоу из действий
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                NoteBundle params = new NoteBundle(title, description, color);
                Intent result = new Intent();
                result.putExtra(EXTRA_PARAMS, params);

                Fragment targetFragment = getTargetFragment();
                if (targetFragment != null) {
                    targetFragment.onActivityResult(REQUEST_CODE, Activity.RESULT_OK, result);
                    fragmentActivity.getSupportFragmentManager().popBackStack();
                }
            }
        });
    }
}