package ru.adnroid.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textView = findViewById(R.id.textViewCity);

        Intent intentCity = getIntent();

        if (intentCity.hasExtra("value")) {
            String value = intentCity.getStringExtra("value");
            textView.setText(value);
        }
    }
}