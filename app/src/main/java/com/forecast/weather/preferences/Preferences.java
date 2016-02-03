package com.forecast.weather.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Aleksei Romashkin
 * on 04/02/16.
 */
public class Preferences {

    private static final String PREFERENCE_FIRST_START = "preference.first_start";

    private SharedPreferences preferences;

    public Preferences(Context context) {
        this.preferences= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFirstStart() {
        this.preferences.edit().putString(PREFERENCE_FIRST_START, "").commit();
    }

    public boolean isAlreadyStarted() {
        return this.preferences.contains(PREFERENCE_FIRST_START);
    }
}
