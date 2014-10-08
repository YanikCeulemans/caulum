package com.sindrave.caelum.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.infrastructure.Cache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Yanik on 15/08/2014.
 */
// TODO: The code for caching could be put in a generic abstract super class instead of an interface...
public class OpenWeatherApi implements WeatherApi {
    private static final String TAG = OpenWeatherApi.class.getName();
    private static final String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s";
    private static final String CACHE_DATA = "com.sindrave.caelum.cache.DATA";
    private static final String CACHE_DATA_DATE = "CACHE_DATA_DATE";
    private static final String CACHE_DATA_CITY = "CACHE_DATA_CITY";
    private static final String CACHE_FILE_NAME = "forecastcache";
    private static final int HOUR_IN_MILLIS = (1000 * 60 * 60);
    private static final int HTTP_OK_RESPONSE_CODE = 200;
    private SharedPreferences preferences;
    private Cache cache;

    public OpenWeatherApi(Context context) {
        preferences = context.getSharedPreferences(CACHE_DATA, Context.MODE_PRIVATE);
        cache = new Cache(context);
    }

    private String getForecastJsonStringForCity(String city) {
        String formattedUrlString = String.format(OPEN_WEATHER_API_URL, city);
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader reader = getReaderFromUrl(formattedUrlString);
            String bufferedJson;
            while ((bufferedJson = reader.readLine()) != null) {
                json.append(bufferedJson).append("\n");
            }
            reader.close();
            Log.d(TAG, "Retrieved JSON String: " + json);
            return json.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + formattedUrlString, e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "unable to open connection to URL: " + formattedUrlString, e);
            return null;
        }
    }

    private JSONObject parseToJsonObject(String jsonData) {
        try {
            JSONObject data = new JSONObject(jsonData);
            if (data.getInt("cod") != HTTP_OK_RESPONSE_CODE) {
                Log.w(TAG, "The service returned a non-OK HTTP response");
                return null;
            }
            return data;
        } catch (JSONException e) {
            Log.e(TAG, "Error trying to parse json: " + jsonData);
            return null;
        }
    }

    private BufferedReader getReaderFromUrl(String formattedUrlString) throws IOException {
        URL apiUrl = new URL(formattedUrlString);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    @Override
    public Forecast getForecast(String city) {
        try {
            if (checkCacheDataIsValid(city)) {
                Forecast forecast = (Forecast) cache.get(CACHE_FILE_NAME);
                Log.d(TAG, "Retrieved forecast from cache: " + forecast.toString());
                return forecast;
            } else {
                String forecastJson = getForecastJsonStringForCity(city);
                JSONObject forecastDataForCity = parseToJsonObject(forecastJson);
                Forecast forecast = OpenWeatherApiParser.parseForecastFromJson(forecastDataForCity);
                cacheData(city, forecast);
                Log.d(TAG, "Retrieved forecast from web: " + forecast.toString());
                return forecast;
            }
        } catch (IOException e) {
            Log.e(TAG, "Cache file does not exist or could not be created", e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Can't cast binary cache data to correct object", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing the json response", e);
            return null;
        }
    }

    private void cacheData(String city, Forecast forecast) throws IOException {
        preferences.edit()
                .putString(CACHE_DATA_CITY, city)
                .putLong(CACHE_DATA_DATE, new Date().getTime())
                .apply();
        cache.save(CACHE_FILE_NAME, forecast);
    }

    private boolean checkCacheDataIsValid(String newCity) {
        String lastCity = preferences.getString(CACHE_DATA_CITY, "");

        long lastCacheTime = preferences.getLong(CACHE_DATA_DATE, 0);
        long now = new Date().getTime();

        return (lastCity != null && lastCity.equals(newCity)) &&
                (lastCacheTime != 0 && now - lastCacheTime < HOUR_IN_MILLIS);
    }
}
