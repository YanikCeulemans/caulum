package com.sindrave.caelum.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yanik on 15/08/2014.
 */
public class SunCycle implements Serializable {
    private final Date sunrise, sunset;

    public SunCycle(long sunrise, long sunset) {
        this.sunrise = new Date(sunrise * 1000);
        this.sunset = new Date(sunset * 1000);
    }

    public Date getSunrise() {
        return sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    @Override
    public String toString() {
        return String.format("sunrise: %tF %tR - sunset: %tF %tR", getSunrise(), getSunrise(), getSunset(), getSunset());
    }
}
