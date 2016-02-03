package com.forecast.weather.http.response;

import com.forecast.weather.http.model.Main;
import com.forecast.weather.http.model.Weather;

import java.util.List;

/**
 * Created by Aleksei Romashkin
 * on 03/02/16.
 */
public class CurrentWeather {
    public List<Weather> weather;
    public Main main;
    public String name;
    public String id;
}
