package ru.adnroid.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private String[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cities = getResources().getStringArray(R.array.cities);
        LinearLayout layoutView = findViewById(R.id.list_container);


        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
            TextView tv = new TextView(this);
            tv.setText(city);
            tv.setTextSize(30);
            layoutView.addView(tv);
            final int index = i;
            tv.setOnClickListener(v -> {
                //открываем новую Активити и передаем туда index
//                    Toast.makeText(MainActivity.this, cities[index], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                String id = String.valueOf(index);
                intent.putExtra("value", id);
                startActivity(intent);
            });
        }
    }


}