package com.example.lyw.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LYW on 2017/1/17.
 */

public class Basic {

    /**
     * "basic": { //基本信息
     * "city": "北京",  //城市名称
     * "cnty": "中国",   //国家
     * "id": "CN101010100",  //城市ID
     * "lat": "39.904000", //城市维度
     * "lon": "116.391000", //城市经度
     * "prov": "北京"  //城市所属省份（仅限国内城市）
     * "update": {  //更新时间
     * "loc": "2016-08-31 11:52",  //当地时间
     * "utc": "2016-08-31 03:52" //UTC时间
     * }
     */
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
