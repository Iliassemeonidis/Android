package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import ru.adnroid.myapplication.utils.ViewUtils;

public class DetailsFragment extends Fragment {

    public static final int REQUEST_CODE = 42;
    public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
    private static final String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    private TextView textViewTitle;
    private TextView textViewDescription;
    private Notes notes;
    private FrameLayout frameLayout;
    private static final String SHARED_PREF_KEY = "SHARED_PREF_KEY";
    private static final String SHARED_PREF_TITLE = "SHARED_PREF_TITLE";
    private static final String SHARED_PREF_DESC = "SHARED_PREF_DESC";
    private static final String SHARED_PREF_COLOR = "SHARED_PREF_COLOR";
    private static final String SHARED_PREF_DEFAULT_VALUE = "SHARED_PREF_DEFAULT_VALUE";

    public static DetailsFragment newInstance(Notes param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA, param1);
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
        notes = Objects.requireNonNull(getArguments()).getParcelable(BUNDLE_EXTRA);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String title = sharedPreferences.getString(SHARED_PREF_TITLE, SHARED_PREF_DEFAULT_VALUE);
        if (title.equals(SHARED_PREF_DEFAULT_VALUE)) {
            textViewTitle.setText(notes.getTitle());
        } else {
            textViewTitle.setText(title);
        }

        textViewDescription.setText(notes.getDescription());
        frameLayout.setBackgroundColor(notes.getColour());
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

                if (ViewUtils.getOrientation(getResources().getConfiguration()) == Configuration.ORIENTATION_LANDSCAPE) {
                    transaction.replace(R.id.details_container, editFragment);
                } else {
                    transaction.replace(R.id.list_container, editFragment);
                }
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
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
        notes = data.getParcelableExtra(EXTRA_PARAMS);
        if (notes != null) {
            setNewNote(notes);
        } else {
            Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNewNote(Notes notes) {
        // Если в портретной ориентации перейти в EditFragment и повернуть экарн в ландшавтную ориентацию и затем нажать сохранить
        //java.lang.NullPointerException: Attempt to invoke virtual method
        //'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
        textViewTitle.setText(notes.getTitle());
        textViewDescription.setText(notes.getDescription());
        frameLayout.setBackgroundColor(notes.getColour());

        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_TITLE, notes.getTitle());
        editor.putString(SHARED_PREF_DESC, notes.getDescription());
        editor.putInt(SHARED_PREF_COLOR, notes.getColour());
        editor.apply();
        setModifiedArg(notes);
    }

    private void setModifiedArg(Notes notes) {
        Bundle modifiedArguments = new Bundle();
        modifiedArguments.putParcelable(BUNDLE_EXTRA, notes);
        setArguments(modifiedArguments);
    }

    //FIXME Save color
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save parcelable
    }
}
