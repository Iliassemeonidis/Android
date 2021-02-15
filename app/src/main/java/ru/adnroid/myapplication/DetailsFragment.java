package ru.adnroid.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class DetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "ARG_PARAM1";



    public static DetailsFragment newInstance(NoteParams param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.title_text_view);
        LinearLayout linearLayout = view.findViewById(R.id.root_action);
        TextView description = view.findViewById(R.id.description_text_view);
        TextView data = view.findViewById(R.id.data_text_view);
        NoteParams noteParams = (NoteParams) Objects.requireNonNull(getArguments()).getParcelable(ARG_PARAM1);
        textView.setText(noteParams.getTitle());
        data.setText((noteParams.getDate()).toString());
        description.setText(noteParams.getDescription());
        linearLayout.setBackgroundColor(noteParams.getColour());
    }




}