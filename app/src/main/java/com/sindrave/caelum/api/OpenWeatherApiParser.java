package com.sindrave.caelum.api;

import android.util.Log;

import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.domain.Location;
import com.sindrave.caelum.domain.SunCycle;
import com.sindrave.caelum.domain.Temperature;
import com.sindrave.caelum.domain.TemperatureForecast;
import com.sindrave.caelum.domain.Weather;
import com.sindrave.caelum.domain.WeatherType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yanik on 14/09/2014.
 */
public class OpenWeatherApiParser {

    public static Forecast parseForecast(JSONObject data) {
        if (data == null) return null;
        try {
            Location location = getLocation(data);
            SunCycle sunCycle = getSunCycle(data);
            JSONObject mainJsonObject = data.getJSONObject("main");
            Weather weather = getWeather(data, mainJsonObject);
            TemperatureForecast tempForecast = getTemperatureForecast(mainJsonObject);
            long date = data.getLong("dt");
            return new Forecast(location, sunCycle, weather, tempForecast, date);
        } catch (JSONException e) {
            Log.e(OpenWeatherApiParser.class.getName(), "Error occurred while parsing JSON", e);
            return null;
        }
    }

    private static TemperatureForecast getTemperatureForecast(JSONObject mainJsonObject) throws JSONException {
        Temperature currentTemperature = new Temperature((float) mainJsonObject.getDouble("temp"), Temperature.Unit.KELVIN);
        Temperature minimumTemperature = new Temperature((float) mainJsonObject.getDouble("temp_min"), Temperature.Unit.KELVIN);
        Temperature maximumTemperature = new Temperature((float) mainJsonObject.getDouble("temp_max"), Temperature.Unit.KELVIN);
        return new TemperatureForecast(currentTemperature, minimumTemperature, maximumTemperature);
    }

    private static Weather getWeather(JSONObject data, JSONObject mainJsonObject) throws JSONException {
        JSONObject firstWeatherJsonObject = (JSONObject) data.getJSONArray("weather").get(0);
        WeatherType weatherType = getWeatherType(firstWeatherJsonObject);
        return new Weather(mainJsonObject.getInt("pressure"), mainJsonObject.getInt("humidity"), firstWeatherJsonObject.getString("main"), firstWeatherJsonObject.getString("description"), weatherType);
    }

    private static WeatherType getWeatherType(JSONObject firstWeatherJsonObject) throws JSONException {
        int weatherConditionId = firstWeatherJsonObject.getInt("id");
        if (weatherConditionId == 800) {
            return WeatherType.CLEAR;
        } else {
            int firstDigit = weatherConditionId / 100;
            switch (firstDigit) {
                case 2:
                    return WeatherType.THUNDER;
                case 3:
                    return WeatherType.DRIZZLE;
                case 7:
                    return WeatherType.FOGGY;
                case 8:
                    return WeatherType.CLOUDY;
                case 6:
                    return WeatherType.SNOWY;
                case 5:
                    return WeatherType.RAINY;
                default:
                    return WeatherType.UNKNOWN;
            }
        }
    }

    private static SunCycle getSunCycle(JSONObject data) throws JSONException {
        JSONObject sysObject = data.getJSONObject("sys");
        return new SunCycle(sysObject.getLong("sunrise"), sysObject.getLong("sunset"));
    }

    private static Location getLocation(JSONObject data) throws JSONException {
        JSONObject jsonCoords = data.getJSONObject("coord");
        String name = data.getString("name");
        float longitude = (float) jsonCoords.getDouble("lon");
        float latitude = (float) jsonCoords.getDouble("lat");
        return new Location(name, longitude, latitude);
    }
}
