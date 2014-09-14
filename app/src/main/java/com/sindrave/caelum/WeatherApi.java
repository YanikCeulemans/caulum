package com.sindrave.caelum;

import android.util.Log;

/**
 * Created by Yanik on 15/08/2014.
 */
public class WeatherApi {
    private final static String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}";

    public WeatherApi() {
    }

    public Forecast getForecast(String city){
        try {
            return null;
        } catch (Exception e) {
            Log.e(WeatherApi.class.getName(), "Error parsing the json response", e);
            return null;
        }
    }
}
