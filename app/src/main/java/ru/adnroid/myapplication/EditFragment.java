package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static ru.adnroid.myapplication.main.MainFragment.EXTRA_PARAMS;

public class EditFragment extends Fragment {

    public static final int REQUEST_CODE_ADD = 42;
    public static final String NOTE_KEY = "NOTE_KEY";
    private static final String NOTE_BUNDLE_EXTRA = "NOTE_BUNDLE_EXTRA";
    private Note noteParams;
    private int color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(NOTE_KEY, noteParams);
    }

    public static EditFragment newInstance(Note param1) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTE_BUNDLE_EXTRA, param1);
        fragment.setArguments(args);
        return fragment;
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
        RadioGroup radioGroup = view.findViewById(R.id.spinner_colors);
        editTextTitle.setText(noteParams.getTitle());
        editTextDescription.setText(noteParams.getDescription());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 1:
                        color = R.color.red;
                        break;
                    case 2:
                        color = R.color.yellow;
                        break;
                    case 3:
                        color = R.color.green;
                        break;
                }
            }
        });
        initButtonSave(view, editTextTitle, editTextDescription);
    }

    private void initButtonSave(@NonNull View view, EditText editTextTitle, EditText editTextDescription) {
        Button buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                if (credentialsAreValid(editTextTitle.getText().toString(), editTextDescription.getText().toString())) {
                    String title = editTextTitle.getText().toString();
                    String description = editTextDescription.getText().toString();
                    int i = color;
                    Note params = new Note(title, description, color);
                    Intent result = new Intent();
                    result.putExtra(EXTRA_PARAMS, params);

                    Fragment targetFragment = getTargetFragment();
                    if (targetFragment != null) {
                        targetFragment.onActivityResult(REQUEST_CODE_ADD, Activity.RESULT_OK, result);
                        fragmentActivity.getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(fragmentActivity, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean credentialsAreValid(String title, String description) {
        int errorCount = 0;
        if (title.isEmpty() || description.isEmpty()) {
            errorCount++;
        } else if (!title.matches(".*\\w.*") || !description.matches(".*\\w.*")) {
            errorCount++;
        }
        return errorCount == 0;
    }
}
