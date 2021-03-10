package ru.adnroid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static ru.adnroid.myapplication.main.MainActivity.EXTRA_KEY;

public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_KEY_RESULT = "EXTRA_KEY_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        EditText editText = findViewById(R.id.edit_query);
        initButtonApply(editText);
    }

    private void initButtonApply(EditText editText) {
        Button apply = findViewById(R.id.button_apply);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_KEY)) {
            String text = intent.getStringExtra(EXTRA_KEY);
            editText.setText(text);
        }
        apply.setOnClickListener(v -> {
            String text = editText.getText().toString();
            Intent result = new Intent();
            result.putExtra(EXTRA_KEY_RESULT, text);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}