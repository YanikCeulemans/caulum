package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 16/08/2014.
 */
public class Weather implements Serializable {

    private final int pressure, humidity, icon;
    private final String shortCurrentWeatherDescription, currentWeatherDescription;

    public Weather(int pressure, int humidity, int icon, String shortCurrentWeatherDescription, String currentWeatherDescription) {
        this.pressure = pressure;
        this.humidity = humidity;
        this.icon = icon;
        this.shortCurrentWeatherDescription = shortCurrentWeatherDescription;
        this.currentWeatherDescription = currentWeatherDescription;
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

    public int getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "pressure=" + pressure +
                ", humidity=" + humidity +
                ", icon=" + icon +
                ", shortCurrentWeatherDescription='" + shortCurrentWeatherDescription + '\'' +
                ", currentWeatherDescription='" + currentWeatherDescription + '\'' +
                '}';
    }
}
