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

import static ru.adnroid.myapplication.DetailsFragment.EXTRA_PARAMS;
import static ru.adnroid.myapplication.DetailsFragment.REQUEST_CODE;

public class EditFragment extends Fragment {

    private static final String NOTE_BUNDLE_EXTRA = "NOTE_BUNDLE_EXTRA";
    public static final String NOTE_KEY = "NOTE_KEY";
    private int color;
    private static Bundle bundle;
    private Note noteParams;

    public static EditFragment newInstance(Note param1) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTE_BUNDLE_EXTRA, param1);
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
        initView(view, savedInstanceState);
        initSpinner(view);
    }

    private void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            noteParams = savedInstanceState.getParcelable(NOTE_KEY);
        } else if (getArguments() != null) {
            noteParams = getArguments().getParcelable(NOTE_BUNDLE_EXTRA);
        } else {
            noteParams = new Note("", "", 0);
        }
        EditText editTextTitle = view.findViewById(R.id.title_edit_text);
        EditText editTextDescription = view.findViewById(R.id.description_edit_text);
        bundle = new Bundle();
        editTextTitle.setText(noteParams.getTitle());
        editTextDescription.setText(noteParams.getDescription());
        initButtonSave(view, editTextTitle, editTextDescription);
    }

    private void initSpinner(@NonNull View view/*, int spinnerPosition*/) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.colors));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerColors = view.findViewById(R.id.spinner_colors);
        spinnerColors.setAdapter(adapter);
          /*if(spinnerPosition >=0){
            //spinner set position
        }*/
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
    }

    private void initButtonSave(@NonNull View view, EditText editTextTitle, EditText editTextDescription) {
        Button buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                Note params = new Note(title, description, color);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(NOTE_KEY, noteParams);
        bundle.putParcelable(NOTE_KEY, noteParams);
    }

    public static Note getNote() {
        return bundle.getParcelable(NOTE_KEY);
    }
}
