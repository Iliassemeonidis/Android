package ru.adnroid.myapplication.utils;

import android.content.res.Configuration;

public final class ViewUtils {
    public static int getOrientation(Configuration configuration) {
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT ? Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }
}
