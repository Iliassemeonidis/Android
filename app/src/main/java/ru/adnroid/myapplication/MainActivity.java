package ru.adnroid.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static ru.adnroid.myapplication.ResultActivity.EXTRA_KEY_RESULT;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 42;
    protected static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String MAIN_FRAGMENT = "MainFragment";
    private static final String DETAILS_FRAGMENT = "DETAILS_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new MainFragment(), R.id.list_container, MAIN_FRAGMENT);
        checkOrientation();
    }

    private void checkOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addFragment(DetailsFragment.newInstance(Notes.getInstance()), R.id.details_container, DETAILS_FRAGMENT);
        }
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

    public void addFragment(Fragment fragment, @IdRes int container, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(container, fragment, tag);
            transaction.commitAllowingStateLoss();
        }
    }
}
