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

import java.util.List;

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
    private FragmentManager fragmentManage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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
        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
//            Toast.makeText(MainActivity.this, "Reselected", Toast.LENGTH_SHORT).show();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //   initDrawer(toolbar);

        if (savedInstanceState == null) {
            addNewFragment(new MainFragment(), MAIN_FRAGMENT_TAG);
        }
//        addFragment(savedInstanceState);
    }

    public void setVisibilityOnItemMenu(boolean b) {
        menuItemSearch.setVisible(b);
        menuItemAdd.setVisible(b);
        menuItemClear.setVisible(b);
    }

//    private void initDrawer(Toolbar toolbar) {
//        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        // Обработка навигационного меню
//        NavigationView navigationView = findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @SuppressLint("NonConstantResourceId")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.my_shopping:
//                        addNewFragment(new ShoppingFragment(), SHOPPING_FRAGMENT_TAG);
//                        break;
//                    case R.id.settings:
//                        addNewFragment(new SettingsFragment(), SETTINGS_FRAGMENT_TAG);
//                        break;
//                    case R.id.saved_notes:
//                        addNewFragment(new MainFragment(), MAIN_FRAGMENT_TAG);
//                        break;
//                }
//                drawer.closeDrawers();
//                return true;
//            }
//        });
//
//        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                navigationView.removeOnLayoutChangeListener(this);
//                TextView textView = (TextView) navigationView.findViewById(R.id.drawerHeaderTitle);
//                textView.setText(R.string.drawer_header_text);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        if (fragmentManage.getBackStackEntryCount() <= 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

            dialog.setTitle("Notification");
            dialog.setMessage("Do you want to leave the application?");
            dialog.setPositiveButton("YES", (dialog1, which) -> finish());
            dialog.setNegativeButton("NO", null);
            dialog.create().show();
        } else {
            super.onBackPressed();
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


//    public void addFragment(Bundle savedInstanceState) {
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            fragmentManage = getSupportFragmentManager();
//            transaction = fragmentManage.beginTransaction();
//            Fragment fragment = fragmentManage.findFragmentById(R.id.list_container);
//            if (savedInstanceState != null) {
//                transaction.replace(R.id.details_container, EditFragment.newInstance(getNote()), EDIT_FRAGMENT_TAG);
//            } else {
//                transaction.replace(R.id.details_container, new EditFragment(), EDIT_FRAGMENT_TAG);
//            }
//            removeFragment(transaction, fragment);
//            transaction.commitAllowingStateLoss();
//        }

//    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    private void addNewFragment(Fragment fragment, String tag) {
        fragmentManage = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManage.beginTransaction();
        List<Fragment> fragments = fragmentManage.getFragments();
        if (fragmentManage.findFragmentByTag(tag) != null) {
            for (int i = 0; i < fragments.size(); i++) {
                String tagToEq = fragments.get(i).getTag();
                if (tagToEq.equals(tag)) {
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

}

