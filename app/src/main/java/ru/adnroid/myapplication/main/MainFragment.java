package ru.adnroid.myapplication.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.adnroid.myapplication.EditFragment;
import ru.adnroid.myapplication.Note;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.utils.ViewUtils;

import static ru.adnroid.myapplication.main.MainFragmentAdapter.HEADER_TYPE;

public class MainFragment extends Fragment {
/*
    по ДЗ:
            -установить проверку заполяняемости полей в EditFragment без Тоста
            -Менять видимость меню в зависимости от открытого фрагмента
            -использовать RadoiGroup и менять фон заметок в зависимости от выбора пользователя*/

    public static final String LIST = "LIST";
    private static final int REQUEST_CODE_EDIT = 42;
    public static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT_TAG";
    public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
    private static Bundle bundle;
    private MainFragmentAdapter adapter;
    private ArrayList<Note> notes;
    private int removePosition;

    private final onClickItem onClickItem = new onClickItem() {
        @Override
        public void onClick(Note note, int position) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            EditFragment editFragment = EditFragment.newInstance(note);
            editFragment.setTargetFragment(MainFragment.this, REQUEST_CODE_EDIT);
            removePosition = position;
            if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                transaction.replace(R.id.details_container, editFragment, EDIT_FRAGMENT_TAG);
            } else {
                transaction.add(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();
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
        FloatingActionButton actionButton = view.findViewById(R.id.floating_action_button);
        actionButton.setOnClickListener(v -> {
            if (notes.isEmpty()) {
                addHeader();
            }
            adapter.appendItem();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                FragmentActivity context = getActivity();
                if (context != null) {
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    EditFragment editFragment = EditFragment.newInstance(new Note());
                    editFragment.setTargetFragment(this, REQUEST_CODE_EDIT);

                    if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                        transaction.replace(R.id.details_container, editFragment, EDIT_FRAGMENT_TAG);
                    } else {
                        transaction.replace(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);
                    }
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                }
                return true;
            case R.id.clear:
                notes.clear();
                adapter.setNewList(notes);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (notes.isEmpty() || notes.size() <= 1) {
                addHeader();
            }
//            notes.remove(removePosition);
            if (data != null) {
                notes.add(1, data.getParcelableExtra(EXTRA_PARAMS));
                adapter.setNewList(notes);
            }

        }
    }

    private void createList(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] cities = getResources().getStringArray(R.array.cities);
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(LIST);
        } else {
            if (notes == null) {
                notes = new ArrayList<>();
                addHeader();
                for (int i = 0; i < cities.length; i++) {
                    String title = cities[i];
                    Note note = new Note(title, "Описание", R.color.green);
                    if (i % 2 == 0) {
                        note.setType(MainFragmentAdapter.NOTE_TYPE);
                    } else {
                        note.setType(MainFragmentAdapter.REMINDER_TYPE);
                        note.setDate("March " + i);
                    }
                    notes.add(note);
                }
            }
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MainFragmentAdapter(notes, onClickItem);
        recyclerView.setAdapter(adapter);
    }

    private void addHeader() {
        notes.add(0, new Note(HEADER_TYPE));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST, notes);
        bundle.putParcelableArrayList(LIST, notes);
    }

    interface onClickItem {
        void onClick(Note notes, int position);
    }
}
