package ru.adnroid.myapplication.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.utils.ViewUtils;

public class MainFragment extends Fragment {
    public static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createList((LinearLayout) view);
    }

    private void createList(@NonNull LinearLayout container) {
        String[] cities = getResources().getStringArray(R.array.cities);
        Notes[] notes = new Notes[cities.length];
        for (String title : cities) {
            Arrays.fill(notes, new Notes(title, "Описание", R.color.purple_700));
        }
        RecyclerView recyclerView = container.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MainFragmentAdapter(notes, requireActivity().getSupportFragmentManager(),ViewUtils.getOrientation(getResources().getConfiguration())));
    }
}
