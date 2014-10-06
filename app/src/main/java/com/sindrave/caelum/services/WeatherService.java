package com.sindrave.caelum.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.sindrave.caelum.MainActivity;
import com.sindrave.caelum.api.WeatherApi;
import com.sindrave.caelum.domain.Forecast;

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
    public static final int HOUR_IN_MILLIS = (1000 * 60 * 60);
    private static SharedPreferences preferences;

    public WeatherService() {
        super("WeatherService");
    }

    public static void startActionCurrentWeather(Context context, String city) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_CURRENT_WEATHER);
        intent.putExtra(EXTRA_LOCATION_CITY, city);
        preferences = context.getSharedPreferences(CACHE_DATA, MODE_PRIVATE);
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
        if (lastCacheTime != 0 && now - lastCacheTime < HOUR_IN_MILLIS) {
            forecast = readForecastFromCache();
        }else {
            forecast = wapi.getForecast(city);
            preferences.edit().putLong(CACHE_DATA_DATE, new Date().getTime()).apply();
            saveForecastToCache(forecast);
        }
        if (forecast != null) {
            broadcastForecast(forecast);
        } else {
            Log.w(TAG, "No forecast data received");
        }
    }

    private void saveForecastToCache(Forecast forecast) {
        File cacheDir = getCacheDir();
        File forecastCache = new File(cacheDir, "forecastcache");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(forecastCache));
            byte[] forecastBytes = toBytes(forecast);
            os.write(forecastBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // TODO exceptions
        } catch (IOException e) {
            e.printStackTrace(); // TODO: exceptions (an error occurred while writing to the stream)
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] toBytes(Forecast forecast) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(forecast);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: Exception
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    private Forecast readForecastFromCache() {
        File cacheDir = getCacheDir();
        File cachedForecast = new File(cacheDir, "forecastcache");
        byte[] fileData = new byte[(int) cachedForecast.length()];
        DataInputStream is = null;
        try {
            is = new DataInputStream(new FileInputStream(cachedForecast));
            is.readFully(fileData);
            Forecast forecast = (Forecast) fromBytes(fileData);
            Log.i(TAG, "Deserialized from cache: " + forecast.toString());
            return forecast;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return null;
    }

    private Object fromBytes(byte[] forecastBytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(forecastBytes);
        try {
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        } catch (IOException e) {
            e.printStackTrace(); // no bytes found
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not find one or more classes in the object graph", e);
            return null;
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
