package com.sindrave.caelum.views;

import android.content.Context;
import android.util.AttributeSet;

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
}
