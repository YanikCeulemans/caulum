package com.sindrave.caelum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView textViewCurrentTemperature;
    private TextView textViewCurrentWeatherDescription;
    private ForecastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(MainActivity.class.getName(), "Thread id: " + Thread.currentThread().getId());
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                WeatherService.startActionCurrentWeather(this, "Kapellen");
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

    private void setCurrentTemperature(float currentTemperature) {
        int currentTemperatureInUnit = Math.round(currentTemperature - 273.15f);
        getTextViewCurrentTemperature().setText(String.format("%d°", currentTemperatureInUnit));
    }

    private String capitalizeFirstCharacter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void setCurrentWeatherDescription(String description) {
        getTextViewCurrentWeatherDescription().setText(capitalizeFirstCharacter(description));
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
            float currentTemperature = forecast.getTemperatureForecast().getCurrentTemperature();
            setCurrentTemperature(currentTemperature);
            setCurrentWeatherDescription(forecast.getWeather().getCurrentWeatherDescription());
        }
    }
}
