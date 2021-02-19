package ru.adnroid.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static ru.adnroid.myapplication.MainActivity.EXTRA_KEY;

public class FragmentActivity extends AppCompatActivity {
    protected static final String EXTRA_KEY_FRAGMENT = "EXTRA_KEY_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        EditText editText = findViewById(R.id.edit_query);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY)) {
            String text = intent.getStringExtra(EXTRA_KEY);
            editText.setText(text);
        }
        Button button = findViewById(R.id.button_apply);

        button.setOnClickListener(v -> {
            String text = editText.getText().toString();
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra(EXTRA_KEY_FRAGMENT, text);
            startActivity(intent1);
        });


    }
}