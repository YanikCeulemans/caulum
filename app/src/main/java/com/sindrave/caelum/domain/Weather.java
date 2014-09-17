package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 16/08/2014.
 */
public class Weather implements Serializable {

    private final int pressure, humidity;
    private final String shortCurrentWeatherDescription, currentWeatherDescription;
    private final WeatherType weatherType;

    public Weather(int pressure, int humidity, String shortCurrentWeatherDescription, String currentWeatherDescription, WeatherType weatherType) {
        this.pressure = pressure;
        this.humidity = humidity;
        this.shortCurrentWeatherDescription = shortCurrentWeatherDescription;
        this.currentWeatherDescription = currentWeatherDescription;
        this.weatherType = weatherType;
    }

    public String getShortCurrentWeatherDescription() {
        return shortCurrentWeatherDescription;
    }

    public String getCurrentWeatherDescription() {
        return currentWeatherDescription;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "pressure=" + pressure +
                ", humidity=" + humidity +
                ", shortCurrentWeatherDescription='" + shortCurrentWeatherDescription + '\'' +
                ", currentWeatherDescription='" + currentWeatherDescription + '\'' +
                ", weatherType=" + weatherType +
                '}';
    }
}
