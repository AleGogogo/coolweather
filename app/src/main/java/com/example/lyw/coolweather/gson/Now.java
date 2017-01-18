package com.example.lyw.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LYW on 2017/1/17.
 */

public class Now {
    /**
     * "cond": {  //天气状况
     "code": "104",  //天气状况代码
     "txt": "阴"  //天气状况描述
     },
     "fl": "11",  //体感温度
     "hum": "31",  //相对湿度（%）
     "pcpn": "0",  //降水量（mm）
     "pres": "1025",  //气压
     "tmp": "13",  //温度
     "vis": "10",  //能见度（km）
     "wind": {  //风力风向
     "deg": "40",  //风向（360度）
     "dir": "东北风",  //风向
     "sc": "4-5",  //风力
     "spd": "24"  //风速（kmph）
     }

     */
      @SerializedName("tmp")
       public String temperature;
      @SerializedName("cond")
       public More more;

       public class More{
           @SerializedName("text")
           public String info;
       }
}
