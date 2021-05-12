package ru.adnroid.myapplication.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import ru.adnroid.myapplication.EditFragment;
import ru.adnroid.myapplication.Note;
import ru.adnroid.myapplication.R;

import static ru.adnroid.myapplication.fragments.SettingsFragment.APP_PREFERENCES_KEY;
import static ru.adnroid.myapplication.fragments.SettingsFragment.APP_PREFERENCES_THEME;

public class MainFragment extends Fragment {

    public static final String LIST = "LIST";
    public static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT_TAG";
    public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
    public static final String APP_NOTE_KEY = "NOTE_KEY";
    public static final String APP_SAVED_NOTE = "APP_SAVED_NOTE";
    public static final String DEFALT_VALUE = "DEFALT_VALUE";
    private static final int REQUEST_CODE_EDIT = 42;
    private static Bundle bundle;
    private MainFragmentAdapter adapter;
    private ArrayList<Note> notes;
    private int removePosition;
    private boolean delete = false;

    private final onClickItem onClickItem = new onClickItem() {
        @Override
        public void onClick(Note note, int position) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            EditFragment editFragment = EditFragment.newInstance(note);
            editFragment.setTargetFragment(MainFragment.this, REQUEST_CODE_EDIT);
            transaction.add(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            delete = true;
            removePosition = position;
        }
    };

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = new Bundle();
        createList(view, savedInstanceState);
        setHasOptionsMenu(true);
        setAppTheme();
        initFloatingActionButton(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST, notes);
        bundle.putParcelableArrayList(LIST, notes);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (delete) {
                notes.remove(removePosition);
            }
            if (data != null) {
                notes.add(data.getParcelableExtra(EXTRA_PARAMS));
                adapter.setNewList(notes);
                saveNoteInPref();
            }
        }
    }

    private void initFloatingActionButton(@NonNull View view) {
        view.findViewById(R.id.floating_action_button).setOnClickListener(v -> {
            createEditFragment();
        });
    }

    private void createEditFragment() {
        FragmentActivity context = getActivity();
        if (context != null) {
            FragmentManager fragmentManager = context.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            EditFragment editFragment = EditFragment.newInstance(new Note());
            editFragment.setTargetFragment(this, REQUEST_CODE_EDIT);
            transaction.replace(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);

            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    private void setAppTheme() {
        SharedPreferences mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        boolean isChecked = mSettings.getBoolean(APP_PREFERENCES_THEME, false);
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void saveNoteInPref() {
        String jsonNotes = getConvertedArrayInStringToJSON();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(APP_NOTE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_SAVED_NOTE, jsonNotes);
        editor.apply();
    }

    private String getConvertedArrayInStringToJSON() {
        return new Gson().toJson(notes);
    }

    private void getTranslationFromStringJSONInArrayList(String s) {
        Type itemsListType = new TypeToken<ArrayList<Note>>() {
        }.getType();
        notes = new Gson().fromJson(s, itemsListType);
        System.out.println(new Gson().fromJson(s, itemsListType).toString());
    }

    private void createList(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences getNotesFromPref = requireContext().getSharedPreferences(APP_NOTE_KEY, Context.MODE_PRIVATE);
        String appNotes = getNotesFromPref.getString(APP_SAVED_NOTE, DEFALT_VALUE);
        if (!appNotes.equals(DEFALT_VALUE)) {
            getTranslationFromStringJSONInArrayList(appNotes);
        } else {
            String[] cities = getResources().getStringArray(R.array.cities);
            if (savedInstanceState != null) {
                notes = savedInstanceState.getParcelableArrayList(LIST);
            } else {
                if (notes == null) {
                    notes = new ArrayList<>();
                    for (String title : cities) {
                        Note note = new Note(title, "Описание", R.color.white);
                        note.setType(MainFragmentAdapter.NOTE_TYPE);
                        notes.add(note);
                    }
                }
            }
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MainFragmentAdapter(notes, onClickItem);
        recyclerView.setAdapter(adapter);
    }

    interface onClickItem {
        void onClick(Note notes, int position);
    }
}
