package com.sindrave.caelum.domain;

import java.io.Serializable;

/**
 * Created by Yanik on 17/09/2014.
 */
public class Temperature implements Serializable {
    private static final float KELVIN_CELCIUS_DIFFERENCE = 273.15f;
    private static Unit unit = Unit.KELVIN;
    /**
     * All amounts are KELVIN internally
     */
    private final float amount;

    public Temperature(float amount, Unit unit) {
        switch (unit) {
            case KELVIN:
                this.amount = amount;
                break;
            case CELSIUS:
                this.amount = getAmountFromCelsius(amount);
                break;
            case FAHRENHEIT:
                this.amount = getAmountFromFahrenheit(amount);
                break;
            default:
                throw new IllegalArgumentException("Illegal unit: " + unit);
        }
    }

    public static Unit getUnit() {
        return unit;
    }

    public static void setUnit(Unit unit) {
        Temperature.unit = unit;
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

    private float getAmountFromCelsius(float amount) {
        return amount + KELVIN_CELCIUS_DIFFERENCE;
    }

    private float getAmountFromFahrenheit(float amount) {
        return (float) (((amount - 32) / 1.8) + KELVIN_CELCIUS_DIFFERENCE);
    }

    @Override
    public String toString() {
        return Math.round(amount) + unit.getSymbol();
    }

    public enum Unit {
        KELVIN,
        CELSIUS,
        FAHRENHEIT;

        public String getSymbol() {
            char symbol;
            switch (this) {
                case KELVIN:
                    symbol = 'K';
                    break;
                case CELSIUS:
                    symbol = 'C';
                    break;
                case FAHRENHEIT:
                    symbol = 'F';
                    break;
                default:
                    throw new UnsupportedOperationException("Uknown unit: + " + this.toString() + ", please add symbol case");
            }
            return "°" + symbol;
        }
    }
}
