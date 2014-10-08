package com.sindrave.caelum.api;

import android.content.Context;
import android.util.Log;

import com.sindrave.caelum.domain.Forecast;

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
public class OpenWeatherApi extends WeatherApi {
    private static final String TAG = OpenWeatherApi.class.getName();
    private static final String OPEN_WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s";
    private static final int HTTP_OK_RESPONSE_CODE = 200;

    public OpenWeatherApi(Context context) {
        super(context);
    }

    private String getForecastJsonStringForCity(String city) {
        String formattedUrlString = String.format(OPEN_WEATHER_API_URL, city);
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader reader = getReaderFromUrl(formattedUrlString);
            String bufferedJson;
            while ((bufferedJson = reader.readLine()) != null) {
                json.append(bufferedJson).append("\n");
            }
            reader.close();
            Log.d(TAG, "Retrieved JSON String: " + json);
            return json.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: " + formattedUrlString, e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "unable to open connection to URL: " + formattedUrlString, e);
            return null;
        }
    }

    private JSONObject parseToJsonObject(String jsonData) {
        try {
            JSONObject data = new JSONObject(jsonData);
            if (data.getInt("cod") != HTTP_OK_RESPONSE_CODE) {
                Log.w(TAG, "The service returned a non-OK HTTP response");
                return null;
            }
            return data;
        } catch (JSONException e) {
            Log.e(TAG, "Error trying to parse json: " + jsonData);
            return null;
        }
    }

    private BufferedReader getReaderFromUrl(String formattedUrlString) throws IOException {
        URL apiUrl = new URL(formattedUrlString);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    @Override
    protected Forecast getForecastCore(String city) {
        String forecastJson = getForecastJsonStringForCity(city);
        JSONObject forecastDataForCity = parseToJsonObject(forecastJson);
        return OpenWeatherApiParser.parseForecastFromJson(forecastDataForCity);
    }
}
