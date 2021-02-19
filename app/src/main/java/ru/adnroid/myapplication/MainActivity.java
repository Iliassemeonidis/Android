package ru.adnroid.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static ru.adnroid.myapplication.FragmentActivity.EXTRA_KEY_FRAGMENT;


public class MainActivity extends AppCompatActivity {
   protected static final String EXTRA_KEY = "EXTRA_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new MainFragment(), "MainFragment");
        Button button = findViewById(R.id.result);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, FragmentActivity.class);
            intent.putExtra(EXTRA_KEY, "Text");
            startActivity(intent);
        });

        Intent intent = getIntent();
        String editText = intent.getStringExtra(EXTRA_KEY_FRAGMENT);
        Toast.makeText(this, editText, Toast.LENGTH_SHORT).show();



    }

    public void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.list_container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }




}