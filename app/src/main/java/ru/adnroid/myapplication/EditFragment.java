package ru.adnroid.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static ru.adnroid.myapplication.main.MainFragment.EXTRA_PARAMS;

public class EditFragment extends Fragment {

    public static final int REQUEST_CODE_ADD = 42;
    public static final String NOTE_KEY = "NOTE_KEY";
    private static final String NOTE_BUNDLE_EXTRA = "NOTE_BUNDLE_EXTRA";
    private Note noteParams;
    private int color;
    private String title;
    private String description;

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
        MenuItem menuItemAdd = menu.findItem(R.id.add_item).setVisible(false);
        MenuItem menuItemClear = menu.findItem(R.id.clear).setVisible(false);
        MenuItem menuItemSearch = menu.findItem(R.id.search).setVisible(false);
//        MenuItem bottomNavigationView = menu.findItem(R.id.settings).setVisible(false);
//        MenuItem bottomNavigationView1 = menu.findItem(R.id.shopping).setVisible(false);
//        MenuItem bottomNavigationView2 = menu.findItem(R.id.saved_notes).setVisible(false);
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
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Такое решение использовал по причине инкрементации переменной checkedId,
                // вероятно проблема в добавлении фрагментов, пока, что не разобрался в этом.
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
            }
        });
        initButtonSave(view, editTextTitle, editTextDescription);
    }

    private void initButtonSave(@NonNull View view, EditText editTextTitle, EditText editTextDescription) {
        Button buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                title = editTextTitle.getText().toString();
                editTextTitle.setOnFocusChangeListener((v12, hasFocus) -> {
                    if (hasFocus) return;
                    TextView tv = (TextView) v12;
                    String value = tv.getText().toString();
                    if (credentialsAreValid(value)) {
                        tv.setError(null);
                        title = editTextTitle.getText().toString();
                    } else {
                        tv.setError(getString(R.string.not_name));
                    }
                });
                description = editTextDescription.getText().toString();
                editTextDescription.setOnFocusChangeListener((v1, hasFocus) -> {
                    if (hasFocus) return;
                    TextView tv = (TextView) v1;
                    String value = tv.getText().toString();
                    if (credentialsAreValid(value)) {
                        tv.setError(null);
                        description = editTextDescription.getText().toString();
                    } else {
                        tv.setError(getString(R.string.not_name_description));
                    }

                });

                if (!title.isEmpty() && !description.isEmpty()) {
                    Note params = new Note(title, description, color);
                    Intent result = new Intent();
                    result.putExtra(EXTRA_PARAMS, params);

                    Fragment targetFragment = getTargetFragment();
                    if (targetFragment != null) {
                        targetFragment.onActivityResult(REQUEST_CODE_ADD, Activity.RESULT_OK, result);
                        fragmentActivity.getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });
    }

    private boolean credentialsAreValid(String title) {
        int errorCount = 0;
        if (title.isEmpty()) {
            errorCount++;
        } else if (!title.matches(".*\\w.*")) {
            errorCount++;
        }
        return errorCount == 0;
    }
}
