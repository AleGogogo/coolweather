

package com.example.lyw.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyw.coolweather.gson.Forecast;
import com.example.lyw.coolweather.gson.Weather;
import com.example.lyw.coolweather.util.HttpUtil;
import com.example.lyw.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView tilteUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView pm25Text;
    private TextView aqiText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    public static final String KEY = "&key=bfda4946a9084c038737e2ab1cfd7c96";
    public static final String HEWEATHER_URL = "http://guolin.tech/api/weather" +
            "?cityid=";
    public static final String SHAR_WEATHER = "weather";
    private static final String TAG = "WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    private void requestWeather(String weatherId) {
        Log.d(TAG, "requestWeather: "+weatherId);
        HttpUtil.sendOkHttpRequest(HEWEATHER_URL + weatherId + KEY, new
                Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast
                                .LENGTH_SHORT).show();
                        Log.d(TAG, "requestFailure......");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws
                            IOException {
                        final String responseText = response.body().string();
                        Log.d(TAG, "responseText is " + responseText);
                        final Weather weather = Utility.handleWeatherResponse
                                (responseText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (weather != null && "ok".equals(weather
                                        .status)) {
                                    SharedPreferences.Editor editor =
                                            PreferenceManager
                                            .getDefaultSharedPreferences
                                                    (WeatherActivity.this)
                                                    .edit();
                                    editor.putString(SHAR_WEATHER,
                                            responseText);
                                    editor.apply();
                                    showWeatherInfo(weather);
                                } else {
                                    Toast.makeText(WeatherActivity.this,
                                            "获取天气信息失败", Toast
                                            .LENGTH_SHORT).show();
                                    Log.d(TAG, "run.....onSuccess");
                                }
                            }
                        });
                    }
                });
    }

    private void showWeatherInfo(Weather weather) {
        titleCity.setText(weather.basic.cityName);
        tilteUpdateTime.setText(weather.basic.update.updateTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature+"℃");
        weatherInfoText.setText(weather.now.more.info);
        Log.d(TAG, "showWeatherInfo: "+weather.now.more.info);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout
                    .forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id
                    .id_forecast_date);
            Log.d(TAG, "dateText is "+dateText);
            TextView infoText = (TextView) view.findViewById(R.id
                    .id_forecast_info);
            TextView maxText = (TextView) view.findViewById(R.id
                    .id_forecast_max);
            TextView minText = (TextView) view.findViewById(R.id
                    .id_forecast_min);
            dateText.setText(forecast.data);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.aqiCity.aqi);
            pm25Text.setText(weather.aqi.aqiCity.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carwash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议: " + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carwash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void initView() {
        weatherLayout = (ScrollView) findViewById(R.id.activity_weather);
        titleCity = (TextView) findViewById(R.id.id_tile_city);
        tilteUpdateTime = (TextView) findViewById(R.id.id_title_time);
        degreeText = (TextView) findViewById(R.id.id_tmp);
        weatherInfoText = (TextView) findViewById(R.id.id_info);
        forecastLayout = (LinearLayout) findViewById(R.id.id_forecast_layout);
        pm25Text = (TextView) findViewById(R.id.id_PM2_5_text);
        aqiText = (TextView) findViewById(R.id.id_aqi_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        carWashText = (TextView) findViewById(R.id.wash_text);
    }

}
