package ru.adnroid.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static ru.adnroid.myapplication.MainActivity.CITY_EXTRA;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView textView = findViewById(R.id.textViewCity);
        textView.setText(getIntent().getStringExtra(CITY_EXTRA));

    }
}