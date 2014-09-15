package com.sindrave.caelum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.helpers.CelciusConverter;
import com.sindrave.caelum.helpers.UnitConverter;
import com.sindrave.caelum.services.WeatherService;


public class MainActivity extends Activity {

    private TextView textViewCurrentTemperature,textViewCurrentWeatherDescription, textViewWeatherIcon;
    private ForecastReceiver receiver;
    private UnitConverter unitConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface weatherTypeFace = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        getTextViewWeatherIcon().setTypeface(weatherTypeFace);

        unitConverter = new CelciusConverter();
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

    public TextView getTextViewWeatherIcon() {
        if (textViewWeatherIcon == null) {
            textViewWeatherIcon = (TextView) findViewById(R.id.textViewCurrentWeatherIcon);
        }
        return textViewWeatherIcon;
    }

    private void setCurrentTemperature(float currentTemperature) {
        int currentTemperatureInUnit = unitConverter.convertToInt(currentTemperature);
        getTextViewCurrentTemperature().setText(String.format("%d°C", currentTemperatureInUnit));
    }

    private String capitalizeFirstCharacter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void setCurrentWeatherDescription(String description) {
        getTextViewCurrentWeatherDescription().setText(capitalizeFirstCharacter(description));
    }

    private void setWeatherIcon(int code) {
        String icon;
        if (code == 800){
            icon = getResources().getString(R.string.weather_icon_clear);
        }else{
            int firstDigit = code / 100;
            switch (firstDigit) {
                case 2:
                    icon = getResources().getString(R.string.weather_icon_thunder);
                    break;
                case 3:
                    icon = getResources().getString(R.string.weather_icon_drizzle);
                    break;
                case 7:
                    icon = getResources().getString(R.string.weather_icon_foggy);
                    break;
                case 8:
                    icon = getResources().getString(R.string.weather_icon_cloudy);
                    break;
                case 6:
                    icon = getResources().getString(R.string.weather_icon_snowy);
                    break;
                case 5:
                    icon = getResources().getString(R.string.weather_icon_rainy);
                    break;
                default:
                    Log.e(MainActivity.class.getName(), "No weather icon found for icon code: " + code);
                    icon = "";
                    break;
            }
        }
        getTextViewWeatherIcon().setText(icon);
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
            setWeatherIcon(forecast.getWeather().getIcon());
        }
    }
}
