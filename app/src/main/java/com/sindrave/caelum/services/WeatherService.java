package com.sindrave.caelum.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.sindrave.caelum.MainActivity;
import com.sindrave.caelum.api.WeatherApi;
import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.infrastructure.Cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

//TODO: Single responsibility !
public class WeatherService extends IntentService {
    private static final String TAG = WeatherService.class.getName();
    private static final String CACHE_DATA = "com.sindrave.caelum.cache.DATA";
    private static final String ACTION_CURRENT_WEATHER = "com.sindrave.caelum.action.CURRENT_WEATHER";
    private static final String EXTRA_LOCATION_CITY = "com.sindrave.caelum.extra.LOCATION_CITY";
    private static final String CACHE_DATA_DATE = "CACHE_DATA_DATE";
    private static final String CACHE_FILE_NAME = "forecastcache";
    private static final int HOUR_IN_MILLIS = (1000 * 60 * 60);
    private static SharedPreferences preferences;
    private static Cache cache;

    public WeatherService() {
        super("WeatherService");
    }

    public static void startActionCurrentWeather(Context context, String city) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_CURRENT_WEATHER);
        intent.putExtra(EXTRA_LOCATION_CITY, city);
        preferences = context.getSharedPreferences(CACHE_DATA, MODE_PRIVATE);
        cache = new Cache(context);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CURRENT_WEATHER.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_LOCATION_CITY);
                handleActionCurrentWeather(param1);
            }
        }
    }

    private void handleActionCurrentWeather(String city) {
        Log.d(TAG, "Thread id: " + Thread.currentThread().getId());
        WeatherApi wapi = new WeatherApi(); // TODO: Dependency injection
        long lastCacheTime = preferences.getLong(CACHE_DATA_DATE, 0);
        long now = new Date().getTime();
        Forecast forecast = null;
        boolean lastCacheTimeShorterThanXTimeAgo = lastCacheTime != 0 && now - lastCacheTime < HOUR_IN_MILLIS;
        if (lastCacheTimeShorterThanXTimeAgo) {
            try {
                forecast = (Forecast) cache.get(CACHE_FILE_NAME);
                Log.i(TAG, "Fetched forecast data from cache: " + forecast.toString());
            } catch (IOException e) { // TODO: Exception handling
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }else {
            forecast = wapi.getForecast(city);
            preferences.edit().putLong(CACHE_DATA_DATE, new Date().getTime()).apply();
            try {
                cache.save(CACHE_FILE_NAME, forecast);
            } catch (IOException e) { // TODO: Exception handling
                e.printStackTrace();
            }
        }
        if (forecast != null) {
            broadcastForecast(forecast);
        } else {
            Log.w(TAG, "No forecast data received");
        }
    }

    private void broadcastForecast(Forecast forecast) {
        if (forecast == null) {
            Log.e(TAG, "Could not parse JSON response");
            return;
        }
        Intent forecastIntent = new Intent();
        forecastIntent.setAction(MainActivity.ForecastReceiver.ACTION_FORECAST_RESOLVED);
        forecastIntent.putExtra(MainActivity.ForecastReceiver.EXTRA_FORECAST, forecast);
        try {
            sendBroadcast(forecastIntent);
        } catch (Exception e) {
            Log.e(TAG, "Error occurred trying to send broadcast intent (Most likely serialization issues)");
        }
    }
}
