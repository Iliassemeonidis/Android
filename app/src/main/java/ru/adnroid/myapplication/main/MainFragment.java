package ru.adnroid.myapplication.main;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.adnroid.myapplication.DetailsFragment;
import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.utils.ViewUtils;

public class MainFragment extends Fragment {

    public static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";
    public static final String BUNDLE = "BUNDLE";
    public static final String LIST = "LIST";
    private static Bundle bundle;
    private MainFragmentAdapter adapter;
    private ArrayList<Notes> note;

    private final onClickItem onClickItem = new onClickItem() {
        @Override
        public void onClick(Notes notes) {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                transaction.replace(R.id.details_container, DetailsFragment.newInstance(notes), DETAILS_FRAGMENT_TAG);
            } else {
                transaction.replace(R.id.list_container, DetailsFragment.newInstance(notes), DETAILS_FRAGMENT_TAG);
            }
            transaction.addToBackStack(null);
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
        switch (item.getItemId()) {
            case R.id.add_item:
                note.add(new Notes("Default", "Default", R.color.purple_700));
//                FragmentActivity context = getActivity();
//                if (context != null) {
//                    FragmentManager fragmentManager = context.getSupportFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    EditFragment editFragment = EditFragment.newInstance(new Notes());
//                    if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
//                        transaction.replace(R.id.details_container, editFragment, EDIT_FRAGMENT_TAG);
//                    } else {
//                        transaction.replace(R.id.list_container, editFragment, EDIT_FRAGMENT_TAG);
//                    }
//                    transaction.addToBackStack(null);
//                }
//                    transaction.commitAllowingStateLoss();
                adapter.setNewList(note);
                return true;
            case R.id.clear:
                note.clear();
                adapter.setNewList(note);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createList(@NonNull LinearLayout container, @Nullable Bundle savedInstanceState) {
        String[] cities = getResources().getStringArray(R.array.cities);
        note = new ArrayList<>();
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelableArrayList(LIST);
        } else {
            for (String title : cities) {
                note.add(new Notes(title, "Описание", R.color.purple_700));
            }
        }
        RecyclerView recyclerView = container.findViewById(R.id.recycler_view);
        adapter = new MainFragmentAdapter(note, onClickItem);
        recyclerView.setAdapter(adapter);
    }

    // FIXME add list
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST, note);
        bundle.putParcelableArrayList(LIST, note);
    }

    public static ArrayList<Notes> getNote() {
        return bundle.getParcelableArrayList(LIST);
    }
}
