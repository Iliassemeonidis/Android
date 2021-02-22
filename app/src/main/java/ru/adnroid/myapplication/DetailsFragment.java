package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;


public class DetailsFragment extends Fragment {
    public static final int REQUEST_CODE = 42;
    public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
    private static final String ARG_PARAM1 = "ARG_PARAM1";
    private TextView textViewTitle;
    private TextView textViewDescription;
    private NoteBundle noteBundle;
    private FrameLayout frameLayout;

    public static DetailsFragment newInstance(NoteBundle param1) {
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
        frameLayout = view.findViewById(R.id.root_action);
        textViewTitle = view.findViewById(R.id.title_text_view);
        textViewDescription = view.findViewById(R.id.description_text_view);
        noteBundle = Objects.requireNonNull(getArguments()).getParcelable(ARG_PARAM1);
        textViewTitle.setText(noteBundle.getTitle());
        textViewDescription.setText(noteBundle.getDescription());
        frameLayout.setBackgroundColor(noteBundle.getColour());

        Button buttonEdit = view.findViewById(R.id.edit_button);
        buttonEdit.setOnClickListener(v -> {
            FragmentActivity fragmentActivity = getActivity();
            if (fragmentActivity != null) {
                FragmentManager fr = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction tr = fr.beginTransaction();
                EditFragment editFragment = EditFragment.newInstance(noteBundle);
                editFragment.setTargetFragment(this, REQUEST_CODE);
                tr.add(R.id.list_container, editFragment);
                tr.addToBackStack(null);
                tr.commitAllowingStateLoss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            checkData(data);
        }
    }

    private void checkData(@Nullable Intent data) {
        if (data == null) {
            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            getData(data);
        }
    }

    private void getData(@NonNull Intent data) {
        noteBundle = data.getParcelableExtra(EXTRA_PARAMS);
        if (noteBundle != null) {
            textViewTitle.setText(noteBundle.getTitle());
            textViewDescription.setText(noteBundle.getDescription());
            frameLayout.setBackgroundColor(noteBundle.getColour());
        } else {
            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();
        }
    }

}