package com.sindrave.caelum;

import java.io.Serializable;

/**
 * Created by Yanik on 15/08/2014.
 */
public class Coords implements Serializable{
    private final float longtitude, latitude;

    public Coords(float longtitude,  float latitude) {
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", getLongtitude(), getLatitude());
    }
}
