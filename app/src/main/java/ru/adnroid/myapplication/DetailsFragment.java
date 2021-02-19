package ru.adnroid.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;


public class DetailsFragment extends Fragment {

    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private static final String EXTRA_PARAMS = "EXTRA_PARAMS";


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
        FrameLayout linearLayout = view.findViewById(R.id.root_action);
        TextView description = view.findViewById(R.id.description_text_view);

        NoteParams noteParams = (NoteParams) Objects.requireNonNull(getArguments()).getParcelable(ARG_PARAM1);
        textView.setText(noteParams.getTitle());

        description.setText(noteParams.getDescription());
        linearLayout.setBackgroundColor(noteParams.getColour());

        Button buttonEdit = view.findViewById(R.id.edit_button);
        buttonEdit.setOnClickListener(v -> {

            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                FragmentManager fr = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction tr = fr.beginTransaction();
                tr.replace(R.id.list_container, new EditFragment());
                tr.addToBackStack(null);
                tr.commitAllowingStateLoss();
//
//                Intent intent = new Intent(fragmentActivity.getBaseContext(), EditFragment.class);
//                intent.putExtra(EXTRA_PARAMS, noteParams);
//                getTargetFragment().onActivityResult(getTargetRequestCode(), -1, intent);
            }
        });
    }
}