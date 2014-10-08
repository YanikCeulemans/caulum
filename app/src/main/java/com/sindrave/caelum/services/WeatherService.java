package com.sindrave.caelum.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sindrave.caelum.MainActivity;
import com.sindrave.caelum.api.OpenWeatherApi;
import com.sindrave.caelum.api.WeatherApi;
import com.sindrave.caelum.domain.Forecast;

public class WeatherService extends IntentService {
    private static final String TAG = WeatherService.class.getName();
    private static final String ACTION_CURRENT_WEATHER = "com.sindrave.caelum.action.CURRENT_WEATHER";
    private static final String EXTRA_LOCATION_CITY = "com.sindrave.caelum.extra.LOCATION_CITY";
    private static WeatherApi weatherApi;

    public WeatherService() {
        super("WeatherService");
    }

    public static void startActionCurrentWeather(Context context, String city) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_CURRENT_WEATHER);
        intent.putExtra(EXTRA_LOCATION_CITY, city);
        weatherApi = new OpenWeatherApi(context);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CURRENT_WEATHER.equals(action)) {
                final String city = intent.getStringExtra(EXTRA_LOCATION_CITY);
                handleActionCurrentWeather(city);
            }
        }
    }

    private void handleActionCurrentWeather(String city) {
        Log.d(TAG, "Thread id: " + Thread.currentThread().getId());
        Forecast forecast = weatherApi.getForecast(city);
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
