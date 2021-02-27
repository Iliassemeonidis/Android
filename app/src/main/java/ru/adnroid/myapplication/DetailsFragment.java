package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
    private Notes notes;
    private FrameLayout frameLayout;

    public static DetailsFragment newInstance(Notes param1) {
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
        initView(view);
    }

    private void initView(@NonNull View view) {
        frameLayout = view.findViewById(R.id.root_action);
        textViewTitle = view.findViewById(R.id.title_text_view);
        textViewDescription = view.findViewById(R.id.description_text_view);
        notes = Objects.requireNonNull(getArguments()).getParcelable(ARG_PARAM1);
        if (textViewTitle != null && textViewDescription != null && frameLayout != null) {
            textViewTitle.setText(notes.getTitle());
            textViewDescription.setText(notes.getDescription());
            frameLayout.setBackgroundColor(notes.getColour());
        }
        initButtonEdit(view);
    }

    private void initButtonEdit(@NonNull View view) {
        Button buttonEdit = view.findViewById(R.id.edit_button);
        buttonEdit.setOnClickListener(v -> {
            FragmentActivity context = getActivity();
            if (context != null) {
                FragmentManager fragment = context.getSupportFragmentManager();
                FragmentTransaction transaction = fragment.beginTransaction();
                EditFragment editFragment = EditFragment.newInstance(notes);
                editFragment.setTargetFragment(this, REQUEST_CODE);
                checkOrientation(transaction, editFragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private void checkOrientation(FragmentTransaction transaction, EditFragment editFragment) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            transaction.replace(R.id.list_container, editFragment);
        } else {
            transaction.replace(R.id.details_container, editFragment);
        }
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
        notes = data.getParcelableExtra(EXTRA_PARAMS);
        if (notes != null) {
            initView(notes);
        } else {
            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(Notes notes) {
        if (textViewTitle != null && textViewDescription != null && frameLayout != null) {
            textViewTitle.setText(notes.getTitle());
            textViewDescription.setText(notes.getDescription());
            frameLayout.setBackgroundColor(notes.getColour());
            getModifiedArg(notes);
        }
    }

    private void getModifiedArg(Notes notes) {
        Bundle modifiedArguments = new Bundle();
        modifiedArguments.putParcelable(ARG_PARAM1, notes);
        setArguments(modifiedArguments);
    }

    //FIXME Save color
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save parcelable
    }

}