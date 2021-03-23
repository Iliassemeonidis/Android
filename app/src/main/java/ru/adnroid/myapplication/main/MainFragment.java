package ru.adnroid.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.adnroid.myapplication.EditFragment;
import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.utils.ViewUtils;

import static ru.adnroid.myapplication.DetailsFragment.EDIT_FRAGMENT_TAG;
import static ru.adnroid.myapplication.DetailsFragment.EXTRA_PARAMS;
import static ru.adnroid.myapplication.DetailsFragment.REQUEST_CODE;
import static ru.adnroid.myapplication.main.MainFragmentAdapter.HEADER_TYPE;

public class MainFragment extends Fragment {

    public static final String BUNDLE = "BUNDLE";
    public static final String LIST = "LIST";
    private static Bundle bundle;
    private MainFragmentAdapter adapter;
    private ArrayList<Notes> notes;

    private final onClickItem onClickItem = new onClickItem() {
        @Override
        public void onClick(Notes notes) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            EditFragment editFragment = EditFragment.newInstance(notes);
            editFragment.setTargetFragment(new MainFragment(), REQUEST_CODE);
            if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                transaction.replace(R.id.details_container, EditFragment.newInstance(notes), EDIT_FRAGMENT_TAG);
//                    transaction.replace(R.id.details_container, editFragment, EDIT_FRAGMENT_TAG);
            } else {
                transaction.add(R.id.list_container, EditFragment.newInstance(notes), EDIT_FRAGMENT_TAG);
//                    transaction.replace(R.id.list_container,editFragment, EDIT_FRAGMENT_TAG);
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();
        }
    };

    public MainFragment() {
    }

    interface onClickItem {
        void onClick(Notes notes);
    }

    public static MainFragment newInstance(ArrayList<Notes> notes) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE, notes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = new Bundle();
        createList((LinearLayout) view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int add_item = R.id.add_item;
        final int clear = R.id.clear;

        switch (item.getItemId()) {
            case add_item:
                FragmentActivity context = getActivity();
                if (context != null) {
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    EditFragment editFragment = EditFragment.newInstance(new Notes());
                    editFragment.setTargetFragment(this, REQUEST_CODE);
                    if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                        transaction.replace(R.id.details_container, editFragment, EDIT_FRAGMENT_TAG);
                    } else {
                        transaction.replace(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);
                    }
                    transaction.addToBackStack(null);
                    transaction.commitAllowingStateLoss();
                }
                return true;
            case clear:
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
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                notes.add(data.getParcelableExtra(EXTRA_PARAMS));
                adapter.setNewList(notes);
            }
        }
    }

    private void createList(@NonNull LinearLayout container, @Nullable Bundle savedInstanceState) {
        String[] cities = getResources().getStringArray(R.array.cities);
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(LIST);
        } else {
            if (notes == null) {
                notes = new ArrayList<>();
                for (int i = 0; i < cities.length; i++) {
                    String title = cities[i];
                    Notes note = new Notes(title, "Описание", R.color.purple_700);
                    if (i == 0) {
                        note.setType(HEADER_TYPE);
                    } else if (i % 2 == 0) {
                        note.setType(MainFragmentAdapter.NOTE_TYPE);
                    } else {
                        note.setType(MainFragmentAdapter.REMINDER_TYPE);
                        note.setDate("March " + i);
                    }
                    notes.add(note);
                }
            }
        }
        RecyclerView recyclerView = container.findViewById(R.id.recycler_view);
        adapter = new MainFragmentAdapter(notes, onClickItem);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST, notes);
        bundle.putParcelableArrayList(LIST, notes);
    }

    public static ArrayList<Notes> getNote() {
        return bundle.getParcelableArrayList(LIST);
    }
}
