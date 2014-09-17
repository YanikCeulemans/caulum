package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 17/09/2014.
 */
public class Temperature implements Serializable{
    /**
     * All amounts are KELVIN internally
     */
    private final float amount;
    private static Unit unit = Unit.KELVIN;

    private static final float KELVIN_CELCIUS_DIFFERENCE = 273.15f;

    public Temperature(float kelvinAmount) {
        this.amount = kelvinAmount;
    }

    public static void setUnit(Unit unit) {
        Temperature.unit = unit;
    }

    public static Unit getUnit() {
        return unit;
    }

    public float getTemperature() {
        switch (unit) {
            case KELVIN:
                return amount;
            case CELSIUS:
                return getAmountInCelsius();
            case FAHRENHEIT:
                return getAmountInFarhenheit();
            default:
                throw new IllegalArgumentException("The unit value: " + unit + " does not exist.");
        }
    }

    private float getAmountInFarhenheit() {
        return (float) (((amount - KELVIN_CELCIUS_DIFFERENCE) * 1.8) + 32);
    }

    private float getAmountInCelsius() {
        return amount - KELVIN_CELCIUS_DIFFERENCE;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "amount=" + amount +
                ", unit=" + unit +
                '}';
    }

    public enum Unit{
        KELVIN,
        CELSIUS,
        FAHRENHEIT;
    }
}
