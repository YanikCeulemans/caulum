package com.sindrave.caelum.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.infrastructure.Cache;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Yanik on 08/10/2014.
 */
public abstract class WeatherApi {
    private static final String TAG = WeatherApi.class.getName();
    private static final String CACHE_DATA = "com.sindrave.caelum.cache.DATA";
    private static final String CACHE_DATA_DATE = "CACHE_DATA_DATE";
    private static final String CACHE_DATA_CITY = "CACHE_DATA_CITY";
    private static final String CACHE_FILE_NAME = "forecastcache";
    private static final int HOUR_IN_MILLIS = (1000 * 60 * 60);
    private SharedPreferences preferences;
    private Cache cache;

    protected WeatherApi(Context context) {
        preferences = context.getSharedPreferences(CACHE_DATA, Context.MODE_PRIVATE);
        cache = new Cache(context);
    }

    public Forecast getForecast(String city) {
        try {
            if (checkCacheDataIsValid(city)) {
                Forecast forecast = (Forecast) cache.get(CACHE_FILE_NAME);
                Log.d(TAG, "Retrieved forecast from cache: " + forecast.toString());
                return forecast;
            } else {
                Forecast forecast = getForecastCore(city);
                cacheData(city, forecast);
                Log.d(TAG, "Retrieved forecast from web: " + forecast.toString());
                return forecast;
            }
        } catch (IOException e) {
            Log.e(TAG, "Cache file does not exist or could not be created", e);
            return null;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Can't cast binary cache data to correct object because the object " +
                    "class definition could not be found", e);
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

    protected abstract Forecast getForecastCore(String city);
}
