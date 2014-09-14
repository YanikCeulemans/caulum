package com.sindrave.caelum;

import java.io.Serializable;

/**
 * Created by Yanik on 14/09/2014.
 */
public class TemperatureForecast implements Serializable {
    /**
     * Unit: Kelvin
     */
    private final float currentTemperature, minimumTemperature, maximumTemperature;

    public TemperatureForecast(float currentTemperature, float minimumTemperature, float maximumTemperature) {
        this.currentTemperature = currentTemperature;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public float getMinimumTemperature() {
        return minimumTemperature;
    }

    public float getMaximumTemperature() {
        return maximumTemperature;
    }

    @Override
    public String toString() {
        return "TemperatureForecast{" +
                "currentTemperature=" + currentTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", maximumTemperature=" + maximumTemperature +
                '}';
    }
}
