package com.sindrave.caelum.domain;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;

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

    private String getTemperatureAmount(NumberFormat numberFormat) {
        float convertedAmount;
        switch (unit) {
            case KELVIN:
                convertedAmount = amount;
                break;
            case CELSIUS:
                convertedAmount = getAmountInCelsius();
                break;
            case FAHRENHEIT:
                convertedAmount = getAmountInFahrenheit();
                break;
            default:
                throw new IllegalArgumentException("The unit value: " + unit + " does not exist.");
        }
        return numberFormat.format(convertedAmount);
    }

    private NumberFormat getNumberFormat(int precision) {
        NumberFormat df = NumberFormat.getNumberInstance();
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(0);
        return df;
    }

    private float getAmountInFahrenheit() {
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
        return toString(0);
    }

    public String toString(int precision) {
        return getTemperatureAmount(getNumberFormat(precision)) + unit.getSymbol();
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
