package com.example.lyw.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyw.coolweather.gson.Weather;
import com.example.lyw.coolweather.util.HttpUtil;

import static com.example.lyw.coolweather.WeatherActivity.SHAR_WEATHER;

public class MainActivity extends AppCompatActivity {
    public static final String URL_WEATHER = "https://free-api.heweather.com/v5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString(SHAR_WEATHER,null)!= null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
