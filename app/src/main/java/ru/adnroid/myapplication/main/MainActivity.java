package ru.adnroid.myapplication.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import ru.adnroid.myapplication.DetailsFragment;
import ru.adnroid.myapplication.EditFragment;
import ru.adnroid.myapplication.Notes;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.ResultActivity;

import static ru.adnroid.myapplication.DetailsFragment.EDIT_FRAGMENT_TAG;
import static ru.adnroid.myapplication.ResultActivity.EXTRA_KEY_RESULT;
import static ru.adnroid.myapplication.main.MainFragment.LIST;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 42;
    public static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";
    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";
    private static final String DEFAULT_TAG = "DEFAULT_TAG";

    private ArrayList<Notes> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainFragment(savedInstanceState);
        addFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initButton() {
        Button button = findViewById(R.id.result);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(EXTRA_KEY, "Text");
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ResultActivity.RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra(EXTRA_KEY_RESULT);
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addMainFragment(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(LIST);
            transaction.replace(R.id.list_container, MainFragment.newInstance(notes), MAIN_FRAGMENT_TAG);
        } else {
            transaction.replace(R.id.list_container, new MainFragment(), MAIN_FRAGMENT_TAG);
        }
        transaction.commitAllowingStateLoss();
    }

    public void addFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(R.id.list_container);
            Notes note;
            if (getFragmentsTag(fragment).equals(MAIN_FRAGMENT_TAG) || getFragmentsTag(fragment).equals(DETAILS_FRAGMENT_TAG)) {
                note = DetailsFragment.getNote();
                transaction.replace(R.id.details_container, DetailsFragment.newInstance(note), DETAILS_FRAGMENT_TAG);
//                removeFragment(transaction, fragment);
            } else if (getFragmentsTag(fragment).equals(EDIT_FRAGMENT_TAG)) {
                note = EditFragment.getNote();
                transaction.add(R.id.details_container, EditFragment.newInstance(note), EDIT_FRAGMENT_TAG);
                removeFragment(transaction, fragment);
            } else {
                note = DetailsFragment.getNote();
                transaction.add(R.id.details_container, DetailsFragment.newInstance(note), DETAILS_FRAGMENT_TAG);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    private String getFragmentsTag(Fragment fragment) {
        if (fragment != null) {
            return fragment.getTag();
        }
        return DEFAULT_TAG;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        notes = MainFragment.getNote();
        outState.putParcelableArrayList(LIST, notes);
    }
}

