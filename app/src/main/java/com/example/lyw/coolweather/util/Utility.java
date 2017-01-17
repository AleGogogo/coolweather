package com.example.lyw.coolweather.util;

import android.text.TextUtils;

import com.example.lyw.coolweather.db.City;
import com.example.lyw.coolweather.db.County;
import com.example.lyw.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LYW on 2017/1/13.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
            if (!TextUtils.isEmpty(response)){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i< jsonArray.length();i++){
                        JSONObject jsonObject = new JSONObject();
                        Province province = new Province();
                        province.setProvinceCode(jsonObject.getInt("id"));
                        province.setProvinceName(jsonObject.getString("name"));
                        province.save();
                    }
                    return true ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return false ;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){

        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0;i< jsonArray.length();i++){
                    JSONObject jsonObject = new JSONObject();
                    City city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false ;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId)  {
          if (!TextUtils.isEmpty(response)){
              try {
                  JSONArray jsonArray = new JSONArray(response);
                  for (int i = 0;i< jsonArray.length();i++){
                      JSONObject jsonObject = new JSONObject();
                      County county = new County();
                      county.setCountyName(jsonObject.getString("name"));
                      county.setCityId(jsonObject.getInt("id"));
                      county.setWeatherId(jsonObject.getInt("weather_id"));
                      county.setCityId(cityId);
                      county.save();
                  }
                  return true ;
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
        return false;
    }
}
