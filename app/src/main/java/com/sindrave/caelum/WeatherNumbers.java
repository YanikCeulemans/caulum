package com.sindrave.caelum;

import java.io.Serializable;

/**
 * Created by Yanik on 15/08/2014.
 */
public class WeatherNumbers implements Serializable {
    /**
     * Unit: Kelvin
     */
    private final float currentTemperature, minimumTemperature, maximumTemperature;
    private final int pressure, humidity;

    public WeatherNumbers(float currentTemperature,float minimumTemperature, float maximumTemperature, int pressure, int humidity) {
        this.currentTemperature = currentTemperature;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
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

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return "WeatherNumbers{" +
                "currentTemperature=" + currentTemperature +
                ", minimumTemperature=" + minimumTemperature +
                ", maximumTemperature=" + maximumTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }
}
