package ru.adnroid.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    static final String CITY_EXTRA = "value";
    private FragmentTransaction  transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoscowFragment moscowFragment = new MoscowFragment();
        PetersburgFragment petersburgFragment = new PetersburgFragment();

        /// Жесточайшая дичь
        LinearLayout layoutView = findViewById(R.id.list_container);
        String[] cities = getResources().getStringArray(R.array.cities);
        for (int i = 0; i < cities.length; i++) {
            String city = cities[i];
            TextView tv = new TextView(this);
            tv.setText(city);
            tv.setTextSize(30);
            layoutView.addView(tv);
            final int index = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    switch (index) {
                        case 0:
                            if (transaction.isAddToBackStackAllowed()) {
                                transaction.remove(petersburgFragment);

                            }
                            transaction.add(R.id.list_container, moscowFragment);
                            break;
                        case 1:
                            if (transaction.isAddToBackStackAllowed()) {
                                transaction.remove(moscowFragment);
                            }
                            transaction.add(R.id.list_container, petersburgFragment);
                            break;
                        default:
                            break;
                    }

                    transaction.commitAllowingStateLoss();
                    //открываем новую Активити и передаем туда index
                }
            });
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.list_container, MainFragment.newInstance("City"));
//        transaction.commitAllowingStateLoss();


    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.list_container, fragment);
        transaction.commitAllowingStateLoss();
    }

}