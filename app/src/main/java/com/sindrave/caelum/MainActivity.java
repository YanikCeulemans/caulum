package com.sindrave.caelum;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.domain.Temperature;
import com.sindrave.caelum.domain.WeatherType;
import com.sindrave.caelum.services.WeatherService;
import com.sindrave.caelum.settings.CitySetting;
import com.sindrave.caelum.settings.TemperatureUnitSetting;
import com.sindrave.caelum.views.WeatherIconView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getName();
    private TextView textViewCurrentTemperature,textViewCurrentWeatherDescription;
    private ForecastReceiver receiver;
    private CitySetting citySetting;
    private TemperatureUnitSetting temperatureUnitSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySetting = new CitySetting(this);
        temperatureUnitSetting = new TemperatureUnitSetting(this);

        Temperature.setUnit(temperatureUnitSetting.getTemperatureUnit());
        getWeather();
    }

    private void getWeather() {
        WeatherService.startActionCurrentWeather(this, citySetting.getCity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i(TAG, "Searched for: " + s);
                citySetting.setCity(s);
                searchItem.collapseActionView();
                getWeather();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Thread id: " + Thread.currentThread().getId());
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                getWeather();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ForecastReceiver.ACTION_FORECAST_RESOLVED);
        receiver = new ForecastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        receiver = null;
    }

    public TextView getTextViewCurrentTemperature() {
        if (textViewCurrentTemperature == null) {
            textViewCurrentTemperature = (TextView) findViewById(R.id.textViewCurrentTemperature);
        }
        return textViewCurrentTemperature;
    }

    public TextView getTextViewCurrentWeatherDescription() {
        if (textViewCurrentWeatherDescription == null) {
            textViewCurrentWeatherDescription = (TextView) findViewById(R.id.textViewCurrentWeatherDescription);
        }
        return textViewCurrentWeatherDescription;
    }

    private void setCurrentTemperature(Temperature currentTemperature) {
        getTextViewCurrentTemperature().setText(currentTemperature.toString());
    }

    private String capitalizeFirstCharacter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void setCurrentWeatherDescription(String description) {
        getTextViewCurrentWeatherDescription().setText(capitalizeFirstCharacter(description));
    }

    private void setWeatherIcon(WeatherType weatherType) {
        WeatherIconView weatherIconView = (WeatherIconView) findViewById(R.id.weatherIconViewCurrentWeatherIcon);
        weatherIconView.setWeatherIcon(weatherType);
    }

    private void setRequestDate(Date date) {
        TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy");
        textViewDate.setText(dateFormatter.format(date));
    }

    private void setLocation(String city) {
        TextView textViewLocation = (TextView) findViewById(R.id.textviewLocationName);
        textViewLocation.setText(city);
    }

    public class ForecastReceiver extends BroadcastReceiver {
        public static final String EXTRA_FORECAST = "com.sindrave.extras.FORECAST";
        public static final String ACTION_FORECAST_RESOLVED = "com.sindrave.action.FORECAST_RESOLVED";

        private Forecast forecast;

        public ForecastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            forecast = (Forecast) intent.getSerializableExtra(EXTRA_FORECAST);
            setCurrentTemperature(forecast.getTemperatureForecast().getCurrentTemperature());
            setCurrentWeatherDescription(forecast.getWeather().getShortCurrentWeatherDescription());
            setWeatherIcon(forecast.getWeather().getWeatherType());
            setRequestDate(forecast.getRequestDate());
            setLocation(forecast.getLocationName());
        }
    }
}
