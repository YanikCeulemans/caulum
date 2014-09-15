package com.sindrave.caelum.api;

import android.util.Log;

import com.sindrave.caelum.domain.Coords;
import com.sindrave.caelum.domain.Forecast;
import com.sindrave.caelum.domain.SunCycle;
import com.sindrave.caelum.domain.TemperatureForecast;
import com.sindrave.caelum.domain.Weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yanik on 14/09/2014.
 */
public class OpenWeatherApiParser {

    public static Forecast parseForecastFromJson(JSONObject data) {
        try {
            Coords location = getLocationFromJson(data);
            SunCycle sunCycle = getSunCycleFromJson(data);
            JSONObject mainJsonObject = data.getJSONObject("main");
            Weather weather = getWeatherFromJson(data, mainJsonObject);
            TemperatureForecast tempForecast = getTemperatureForecastFromJson(mainJsonObject);
            long date = data.getLong("dt");
            return new Forecast(location, sunCycle, weather, tempForecast, date);
        } catch (JSONException e) {
            Log.e(OpenWeatherApiParser.class.getName(), "Error occurred while parsing JSON", e);
            return null;
        }
    }

    private static TemperatureForecast getTemperatureForecastFromJson(JSONObject mainJsonObject) throws JSONException {
        return new TemperatureForecast((float)mainJsonObject.getDouble("temp"), (float)mainJsonObject.getDouble("temp_min"), (float)mainJsonObject.getDouble("temp_max"));
    }

    private static Weather getWeatherFromJson(JSONObject data, JSONObject mainJsonObject) throws JSONException {
        JSONObject firstWeatherJsonObject = (JSONObject) data.getJSONArray("weather").get(0);
        return new Weather(mainJsonObject.getInt("pressure"), mainJsonObject.getInt("humidity"), firstWeatherJsonObject.getString("description"),firstWeatherJsonObject.getString("description"));
    }

    private static SunCycle getSunCycleFromJson(JSONObject data) throws JSONException {
        JSONObject sysObject = data.getJSONObject("sys");
        return new SunCycle(sysObject.getLong("sunrise"), sysObject.getLong("sunset"));
    }

    private static Coords getLocationFromJson(JSONObject data) throws JSONException {
        JSONObject jsonCoords = data.getJSONObject("coord");
        return new Coords((float)jsonCoords.getDouble("lon"), (float)jsonCoords.getDouble("lat"));
    }
}
