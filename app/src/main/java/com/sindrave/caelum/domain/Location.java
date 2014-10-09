package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 09/10/2014.
 */
public class Location implements Serializable {
    private final float longitude, latitude;
    private final String name;

    public Location(String name, float longtitude, float latitude) {
        this.name = name;
        this.longitude = longtitude;
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                '}';
    }
}
