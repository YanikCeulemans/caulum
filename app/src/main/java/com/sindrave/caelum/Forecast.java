package com.sindrave.caelum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Yanik on 15/08/2014.
 */
public class Forecast implements Serializable{

    private final Coords coords;
    private final SunCycle sunCycle;
    private final Weather[] weather;
    private final WeatherNumbers weatherNumbers;
    private final Date requestDate;

    public Forecast
            (Coords coords,
             SunCycle sunCycle,
             Weather[] weather,
             WeatherNumbers weatherNumbers,
             long requestDate) {
        this.coords = coords;
        this.sunCycle = sunCycle;
        this.weather = weather;
        this.weatherNumbers = weatherNumbers;
        this.requestDate = new Date(requestDate * 1000);
    }

    public Coords getCoords() {
        return coords;
    }

    public SunCycle getSunCycle() {
        return sunCycle;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public WeatherNumbers getWeatherNumbers() {
        return weatherNumbers;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "coords=" + coords +
                ", sunCycle=" + sunCycle +
                ", weather=" + Arrays.toString(weather) +
                ", weatherNumbers=" + weatherNumbers +
                ", requestDate=" + requestDate +
                '}';
    }
}
