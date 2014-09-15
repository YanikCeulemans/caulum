package com.sindrave.caelum.helpers;

/**
 * Created by Yanik on 15/09/2014.
 */
public class CelciusConverter implements UnitConverter {

    private static final float KELVIN_CELCIUS_DIFFERENCE = 273.15f;

    @Override
    public float convertFloat(float source) {
        return source - KELVIN_CELCIUS_DIFFERENCE;
    }

    @Override
    public double convertDouble(double source) {
        return source - KELVIN_CELCIUS_DIFFERENCE;
    }

    @Override
    public int convertInt(int source) {
        return Math.round(source - KELVIN_CELCIUS_DIFFERENCE);
    }

    @Override
    public int convertToInt(float source) {
        return Math.round(convertFloat(source));
    }

    @Override
    public int convertToInt(double source) {
        return Math.round(convertFloat((float) source));
    }
}
