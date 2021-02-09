package ru.adnroid.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static ru.adnroid.myapplication.MainActivity.CITY_EXTRA;

public class MainFragment extends Fragment {

    private static final String CITY_FRAGMENT_EXTRA = "CITY_FRAGMENT_EXTRA";

    public MainFragment() {
    }

    public static MainFragment newInstance(String city) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(CITY_FRAGMENT_EXTRA, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            getArguments().getString(CITY_FRAGMENT_EXTRA);
        }
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String[] cities = getResources().getStringArray(R.array.cities);
//        LinearLayout layoutView = (LinearLayout) view;
//        for (String city : cities) {
//            Context context = requireContext();
//            TextView tv = new TextView(context);
//            tv.setText(city);
//            tv.setTextSize(30);
//            layoutView.addView(tv);
//            tv.setOnClickListener(v -> {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra(CITY_EXTRA, city);
//                startActivity(intent);
//            });
//        }


    }
}
