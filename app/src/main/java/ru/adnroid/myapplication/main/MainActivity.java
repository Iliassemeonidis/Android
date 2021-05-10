package ru.adnroid.myapplication.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import ru.adnroid.myapplication.R;
import ru.adnroid.myapplication.fragments.SettingsFragment;
import ru.adnroid.myapplication.fragments.ShoppingFragment;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 42;
    public static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";
    private static final String SETTINGS_FRAGMENT_TAG = "SETTINGS_FRAGMENT_TAG";
    private static final String SHOPPING_FRAGMENT_TAG = "SHOPPING_FRAGMENT_TAG";
    private static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT_TAG";
    private MenuItem menuItemSearch;
    private MenuItem menuItemAdd;
    private MenuItem menuItemClear;
    private static BottomNavigationView bottomNavigationView;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Измененение у меня происходит в SettingsFragment, не в MainActivity.
        // Не понимаю зачем устанавливать тему тут?
        // setTheme(); - это решение предложил ты.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        initBottomNavigation();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            addNewFragment(new MainFragment(), MAIN_FRAGMENT_TAG);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.my_shopping:
                    addNewFragment(new ShoppingFragment(), SHOPPING_FRAGMENT_TAG);
                    setVisibilityOnItemMenu(false);
                    break;
                case R.id.settings:
                    addNewFragment(new SettingsFragment(), SETTINGS_FRAGMENT_TAG);
                    setVisibilityOnItemMenu(false);
                    break;
                case R.id.saved_notes:
                    addNewFragment(new MainFragment(), MAIN_FRAGMENT_TAG);
                    setVisibilityOnItemMenu(true);
                    break;
            }
            return true;
        });
    }

    public void setVisibilityOnItemMenu(boolean b) {
        menuItemSearch.setVisible(b);
        menuItemAdd.setVisible(b);
        menuItemClear.setVisible(b);
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
            setVisibilityInNavigation(true);
            setVisibilityInItemMenuIfMainFragmentIsVisible();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);

        menuItemSearch = menu.findItem(R.id.search);
        menuItemAdd = menu.findItem(R.id.add_item);
        menuItemClear = menu.findItem(R.id.clear);

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

    public static void setVisibilityInNavigation(boolean isVisible) {
        if (isVisible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.remove(fragment);
        }
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

