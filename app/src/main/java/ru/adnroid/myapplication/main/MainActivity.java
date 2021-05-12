package ru.adnroid.myapplication.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Objects;

import ru.adnroid.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";
    private static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG";
    private MenuItem menuItemSearch;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            addNewFragment(new MainFragment(), MAIN_FRAGMENT_TAG);
        }
    }

    public void setVisibilityOnItemMenu(boolean b) {
        menuItemSearch.setVisible(b);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

            dialog.setTitle("Notification");
            dialog.setMessage("Do you want to leave the application?");
            dialog.setPositiveButton("YES", (dialog1, which) -> finish());
            dialog.setNegativeButton("NO", null);
            dialog.create().show();
        } else {
            super.onBackPressed();
            setVisibilityInItemMenuIfMainFragmentIsVisible();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);

        menuItemSearch = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return true;
    }

    // TODO разобраться с бодавление фрагментов
    private void addNewFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragmentManager.findFragmentByTag(tag) != null) {
            for (int i = 0; i < fragments.size(); i++) {
                String tagToEq = fragments.get(i).getTag();
                if (Objects.requireNonNull(tagToEq).equals(tag)) {
                    backToFragment(fragments.get(i).getClass().getName());
                    fragments.clear();
                }
            }
        } else {
            transaction.add(R.id.list_container, fragment, tag);
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
    }

    private void backToFragment(String fragment) {
        // возвращаемся к тому, что было добавлено в backstack
        getSupportFragmentManager().popBackStack(fragment, 0);
    }

    private void setVisibilityInItemMenuIfMainFragmentIsVisible() {
        FragmentManager fragmentManage = getSupportFragmentManager();
        Fragment fragment = fragmentManage.findFragmentById(R.id.list_container);
        if (fragment.getTag().equals(MAIN_FRAGMENT_TAG)) {
            setVisibilityOnItemMenu(true);
        }
    }
}

