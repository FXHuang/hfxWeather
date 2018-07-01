package com.fxhuang.hfxweather.gson;

import com.google.gson.annotations.SerializedName;


//天气实况类
public class Now {
    @SerializedName("cond_txt")
    public String info;  //当前天气 如"晴"
    @SerializedName("hum")
    public String relativeHumidity; //相对湿度

    @SerializedName("fl")
    public String feelingTem; //体感温度
    @SerializedName("tmp")
    public String temperature; //温度

    @SerializedName("pcpn")
    public String jiangShui; //降水量
    @SerializedName("wind_dir")
    public String windDirec;  //风向
    @SerializedName("wind_sc")
    public String windStrength; //风力
    @SerializedName("wind_spd")
    public String windSpeed; //风速
    @SerializedName("vis")
    public String viewDistance;  //能见度
}
