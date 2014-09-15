package com.sindrave.caelum.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.sindrave.caelum.MainActivity;
import com.sindrave.caelum.api.WeatherApi;
import com.sindrave.caelum.domain.Forecast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CURRENT_WEATHER = "com.sindrave.caelum.action.CURRENT_WEATHER";
    private static final String ACTION_BAZ = "com.sindrave.caelum.action.BAZ";

    private static final String EXTRA_LOCATION_CITY = "com.sindrave.caelum.extra.LOCATION_CITY";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionCurrentWeather(Context context, String city) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_CURRENT_WEATHER);
        intent.putExtra(EXTRA_LOCATION_CITY, city);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1) {
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_LOCATION_CITY, param1);
        context.startService(intent);
    }

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CURRENT_WEATHER.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_LOCATION_CITY);
                handleActionCurrentWeather(param1);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_LOCATION_CITY);
                handleActionBaz(param1);
            }
        }
    }

    private void handleActionCurrentWeather(String city) {
        Log.d(WeatherService.class.getName(), "Thread id: " + Thread.currentThread().getId());
        WeatherApi wapi = new WeatherApi();
        Forecast result = wapi.getForecast(city);
        if (result != null) {
            Intent forecastIntent = new Intent();
            forecastIntent.setAction(MainActivity.ForecastReceiver.ACTION_FORECAST_RESOLVED);
            forecastIntent.putExtra(MainActivity.ForecastReceiver.EXTRA_FORECAST, result);
            try {
                sendBroadcast(forecastIntent);
            } catch (Exception e) {
                Log.e(WeatherService.class.getName(), "Error occurred trying to send broadcast intent (Most likely serialization issues)");
            }
        }else{
            Log.e(WeatherService.class.getName(), "Could not parse JSON response");
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
