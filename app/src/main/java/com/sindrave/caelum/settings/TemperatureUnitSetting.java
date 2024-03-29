package com.sindrave.caelum.settings;

import android.app.Activity;
import android.content.SharedPreferences;

import com.sindrave.caelum.domain.Temperature;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yanik on 17/09/2014.
 */
public class TemperatureUnitSetting {
    private static final List<Temperature.Unit> UNIT_VALUES = Arrays.asList(Temperature.Unit.values());
    private static final String PREFERENCE_TEMPERATURE_UNIT = "preference_temperature_unit";
    private static final int DEFAULT_UNIT_INDEX = UNIT_VALUES.indexOf(Temperature.Unit.CELSIUS);

    private SharedPreferences preferences;

    public TemperatureUnitSetting(Activity activity) {
        this.preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public Temperature.Unit getTemperatureUnit() {
        return UNIT_VALUES.get(preferences.getInt(PREFERENCE_TEMPERATURE_UNIT, DEFAULT_UNIT_INDEX));
    }

    public void setTemperatureUnit(Temperature.Unit unit) {
        int unitIndex = UNIT_VALUES.indexOf(unit);
        preferences.edit().putInt(PREFERENCE_TEMPERATURE_UNIT, unitIndex).apply();
    }
}
