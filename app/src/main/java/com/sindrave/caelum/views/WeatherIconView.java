package com.sindrave.caelum.views;

import android.content.Context;
import android.util.AttributeSet;

import com.sindrave.caelum.R;
import com.sindrave.caelum.domain.WeatherType;

/**
 * Created by Yanik on 16/09/2014.
 */
public class WeatherIconView extends TypefaceView {
    private static final String WEATHER_FONT = "weather.ttf";

    public WeatherIconView(Context context) {
        super(context);
        setCustomTypeface(context, WEATHER_FONT);
    }

    public WeatherIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(context, WEATHER_FONT);
    }

    public WeatherIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomTypeface(context, WEATHER_FONT);
    }

    public void setWeatherIcon(WeatherType weatherType) {
        String weatherIconString;
        switch (weatherType) {
            case CLEAR:
                weatherIconString = getResources().getString(R.string.weather_icon_clear);
                break;
            case CLOUDY:
                weatherIconString = getResources().getString(R.string.weather_icon_cloudy);
                break;
            case FOGGY:
                weatherIconString = getResources().getString(R.string.weather_icon_foggy);
                break;
            case RAINY:
                weatherIconString = getResources().getString(R.string.weather_icon_rainy);
                break;
            case THUNDER:
                weatherIconString = getResources().getString(R.string.weather_icon_thunder);
                break;
            case SNOWY:
                weatherIconString = getResources().getString(R.string.weather_icon_snowy);
                break;
            case DRIZZLE:
                weatherIconString = getResources().getString(R.string.weather_icon_drizzle);
                break;
            default:
                weatherIconString = "";
                break;
        }
        setText(weatherIconString);
    }

}
