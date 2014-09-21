package com.sindrave.caelum.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

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

    private String getTemperature() {
        return getTemperature(0);
    }

    private String getTemperature(int precision) {
        float convertedAmount;
        switch (unit) {
            case KELVIN:
                convertedAmount = amount;
                break;
            case CELSIUS:
                convertedAmount = getAmountInCelsius();
                break;
            case FAHRENHEIT:
                convertedAmount = getAmountInFarhenheit();
                break;
            default:
                throw new IllegalArgumentException("The unit value: " + unit + " does not exist.");
        }
        DecimalFormat df = new DecimalFormat(getPrecisionFormatString(precision));
        return df.format(convertedAmount);

    }

    private String getPrecisionFormatString(int precision) {
        StringBuilder strb = new StringBuilder("#");
        if (precision <= 0) {
            return "";
        } else {
            strb.append(".");
            for (int i = 0; i < precision; i++) {
                strb.append("#");
            }
        }
        return strb.toString();
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
        return getTemperature() + unit.getSymbol();
    }

    public String toString(int precision) {
        return getTemperature(precision) + unit.getSymbol();
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
