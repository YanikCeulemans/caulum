package com.sindrave.caelum.api;

import com.sindrave.caelum.domain.Forecast;

/**
 * Created by Yanik on 08/10/2014.
 */
public interface WeatherApi {
    Forecast getForecast(String city);
}
