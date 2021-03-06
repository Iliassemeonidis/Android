package ru.adnroid.myapplication.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.adnroid.myapplication.EditFragment;
import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.ResultActivity;

import static ru.adnroid.myapplication.DetailsFragment.EDIT_FRAGMENT_TAG;
import static ru.adnroid.myapplication.EditFragment.getNote;
import static ru.adnroid.myapplication.ResultActivity.EXTRA_KEY_RESULT;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 42;
    public static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            addMainFragment();
        }
        addFragment(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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

    public void addMainFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.list_container, new MainFragment(), MAIN_FRAGMENT_TAG);
        transaction.commitAllowingStateLoss();
    }

    public void addFragment(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(R.id.list_container);
            if (savedInstanceState != null) {
                transaction.replace(R.id.details_container, EditFragment.newInstance(getNote()), EDIT_FRAGMENT_TAG);
            } else {
                transaction.replace(R.id.details_container, new EditFragment(), EDIT_FRAGMENT_TAG);
            }
            removeFragment(transaction, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }
}

