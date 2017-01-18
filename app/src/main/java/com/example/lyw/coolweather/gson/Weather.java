package com.example.lyw.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.BitSet;
import java.util.List;

/**
 * Created by LYW on 2017/1/17.
 */

public class Weather {

    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public  Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
