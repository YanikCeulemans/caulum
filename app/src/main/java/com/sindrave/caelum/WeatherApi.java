package com.sindrave.caelum;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Yanik on 15/08/2014.
 */
public class WeatherApi {
    private final static String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    public static final int HTTP_OK_RESPONSE_CODE = 200;

    private JSONObject getForecastDataForCity(String city) {
        String formattedUrlString = String.format(OPEN_WEATHER_API_URL, city);
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader reader = getReaderFromUrl(formattedUrlString);
            String bufferedJson;
            while ((bufferedJson = reader.readLine()) != null) {
                json.append(bufferedJson).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("cod") != HTTP_OK_RESPONSE_CODE) {
                Log.w(WeatherApi.class.getName(), "The service returned a non-OK HTTP response");
                return null;
            }
            return data;
        } catch (MalformedURLException e) {
            Log.e(WeatherApi.class.getName(), "Malformed URL: " + formattedUrlString, e);
            return null;
        } catch (IOException e) {
            Log.e(WeatherApi.class.getName(), "unable to open connection to URL: " + formattedUrlString, e);
            return null;
        } catch (JSONException e) {
            Log.e(WeatherApi.class.getName(), "Error parsing json: " + json.toString());
            return null;
        }
    }

    private BufferedReader getReaderFromUrl(String formattedUrlString) throws IOException {
        URL apiUrl = new URL(formattedUrlString);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    public Forecast getForecast(String city){
        try {
            JSONObject forecastDataForCityJson = getForecastDataForCity(city);
            return OpenWeatherApiParser.parseForecastFromJson(forecastDataForCityJson);
        } catch (Exception e) {
            Log.e(WeatherApi.class.getName(), "Error parsing the json response", e);
            return null;
        }
    }
}
