package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 14/09/2014.
 */
public class TemperatureForecast implements Serializable {
    /**
     * Unit: KELVIN
     */
    private final Temperature currentTemperature, minimumTemperature, maximumTemperature;

    public TemperatureForecast(Temperature currentTemperature, Temperature minimumTemperature, Temperature maximumTemperature) {
        this.currentTemperature = currentTemperature;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
    }

    public Temperature getCurrentTemperature() {
        return currentTemperature;
    }

    public Temperature getMinimumTemperature() {
        return minimumTemperature;
    }

    public Temperature getMaximumTemperature() {
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
