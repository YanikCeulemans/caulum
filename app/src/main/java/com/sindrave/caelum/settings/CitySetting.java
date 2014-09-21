package com.sindrave.caelum.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yanik on 16/09/2014.
 */
public class CitySetting {
    private static final String PREFERENCE_CITY = "preference_city";
    private SharedPreferences preferences;

    public CitySetting(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return preferences.getString(PREFERENCE_CITY, "Kapellen");
    }

    public void setCity(String city) {
        preferences.edit().putString(PREFERENCE_CITY, city).apply();
    }
}
