package com.sindrave.caelum;

import java.io.Serializable;

/**
 * Created by Yanik on 16/08/2014.
 */
public class Weather implements Serializable {
    private final String currentWeather, currentWeatherDescription;


    public Weather(String currentWeather, String currentWeatherDescription) {
        this.currentWeather = currentWeather;
        this.currentWeatherDescription = currentWeatherDescription;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public String getCurrentWeatherDescription() {
        return currentWeatherDescription;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getCurrentWeather(), getCurrentWeatherDescription());
    }
}
