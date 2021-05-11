package ru.adnroid.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ru.adnroid.myapplication.main.MainActivity;

import static ru.adnroid.myapplication.main.MainFragment.EXTRA_PARAMS;

public class EditFragment extends Fragment {

    public static final int REQUEST_CODE_ADD = 42;
    public static final String NOTE_KEY = "NOTE_KEY";
    private static final String NOTE_BUNDLE_EXTRA = "NOTE_BUNDLE_EXTRA";

    private Note noteParams;
    private int color;
    private TextInputLayout textInputLayoutTitle;
    private TextInputEditText editTextTitle;
    private TextInputLayout textInputLayoutDescription;
    private TextInputEditText editTextDescription;

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(NOTE_KEY, noteParams);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
    }

    private void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setNoteParams(savedInstanceState);
        initEditTexts(view);
        initColorSelection(view);
        initButtonSave(view);
    }

    @SuppressLint("NonConstantResourceId")
    private void initColorSelection(@NonNull View view) {
        RadioGroup radioGroup = view.findViewById(R.id.spinner_colors);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton button = group.findViewById(checkedId);
            switch (button.getId()) {
                case R.id.red:
                    color = R.color.red;
                    break;
                case R.id.yellow:
                    color = R.color.yellow;
                    break;
                case R.id.green:
                    color = R.color.green;
                    break;
            }
        });
    }

    private void initEditTexts(@NonNull View view) {
        editTextTitle = view.findViewById(R.id.title_edit_text);
        editTextTitle.setText(noteParams.getTitle());
        textInputLayoutTitle = view.findViewById(R.id.edit_fragment_title_til);
        textInputLayoutDescription = view.findViewById(R.id.description);
        editTextDescription = view.findViewById(R.id.description_edit_text);
        editTextDescription.setText(noteParams.getDescription());
    }

    private void setNoteParams(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            noteParams = savedInstanceState.getParcelable(NOTE_KEY);
        } else if (getArguments() != null) {
            noteParams = getArguments().getParcelable(NOTE_BUNDLE_EXTRA);
        } else {
            noteParams = new Note("", "", 0);
        }
    }

    private void initButtonSave(@NonNull View view) {
        view.findViewById(R.id.button_save).setOnClickListener(v -> onButtonSaveClick());
    }

    private void onButtonSaveClick() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (credentialsAreValid(title, description)) {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                saveNote(title, description, fragmentActivity);
            }
        }
    }

    private void saveNote(String title, String description, FragmentActivity fragmentActivity) {
        Note params = new Note(title, description, color);
        Intent result = new Intent();
        result.putExtra(EXTRA_PARAMS, params);

        checkTargetIsNull(fragmentActivity, result);
    }

    private void checkTargetIsNull(FragmentActivity fragmentActivity, Intent result) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            sendActivityResult(fragmentActivity, result, targetFragment);
        } else {
            Log.i("NULL", "TargetFragment is null");
        }
    }

    private void sendActivityResult(FragmentActivity fragmentActivity, Intent result, Fragment targetFragment) {
        targetFragment.onActivityResult(REQUEST_CODE_ADD, Activity.RESULT_OK, result);
        fragmentActivity.getSupportFragmentManager().popBackStack();
    }

    private boolean checkString(String title) {
        int errorCount = 0;
        if (title.isEmpty()) {
            errorCount++;
        } else if (!title.matches(".*\\w.*")) {
            errorCount++;
        }
        return errorCount != 0;
    }

    private boolean credentialsAreValid(String title, String description) {
        int errorCount = 0;
        if (checkString(title)) {
            textInputLayoutTitle.setErrorEnabled(true);
            textInputLayoutTitle.setError(title.isEmpty() ? getString(R.string.error_title) : getString(R.string.incorrect));
            errorCount++;
        } else {
            textInputLayoutTitle.setErrorEnabled(false);
        }
        if (checkString(description)) {
            textInputLayoutDescription.setErrorEnabled(true);
            textInputLayoutDescription.setError(description.isEmpty() ? getString(R.string.error_description) : getString(R.string.incorrect));
            errorCount++;
        } else {
            textInputLayoutDescription.setErrorEnabled(false);
        }
        return errorCount == 0;
    }
}
