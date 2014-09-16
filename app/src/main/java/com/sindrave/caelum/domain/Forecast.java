package com.sindrave.caelum.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yanik on 15/08/2014.
 */
public class Forecast implements Serializable{

    private final Coords location;
    private final String locationName;
    private final SunCycle sunCycle;
    private final Weather weather;
    private final TemperatureForecast temperatureForecast;
    private final Date requestDate;

    public Forecast
            (Coords location,
             String locationName,
             SunCycle sunCycle,
             Weather weather,
             TemperatureForecast temperatureForecast,
             long requestDate) {
        this.location = location;
        this.locationName = locationName;
        this.sunCycle = sunCycle;
        this.weather = weather;
        this.temperatureForecast = temperatureForecast;
        this.requestDate = new Date(requestDate * 1000);
    }

    public Coords getLocation() {
        return location;
    }

    public String getLocationName() {
        return locationName;
    }

    public SunCycle getSunCycle() {
        return sunCycle;
    }

    public Weather getWeather() {
        return weather;
    }

    public TemperatureForecast getTemperatureForecast() {
        return temperatureForecast;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "location=" + location +
                ", locationName='" + locationName + '\'' +
                ", sunCycle=" + sunCycle +
                ", weather=" + weather +
                ", temperatureForecast=" + temperatureForecast +
                ", requestDate=" + requestDate +
                '}';
    }
}
