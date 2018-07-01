package com.fxhuang.hfxweather.gson;

import com.google.gson.annotations.SerializedName;



public class Forecast {
    public String date;
    @SerializedName("cond_txt_d")
    public String dayInfo; //白天天气
    @SerializedName("cond_txt_n")
    public String nightInfo; //夜间天气

    @SerializedName("tmp_max")
    public String temperatureMax; //最高温度
    @SerializedName("tmp_min")
    public String temperatureMin; //最低温度

    @SerializedName("pcpn")
    public String jiangShui; //降水量
    @SerializedName("pop")
    public String jiangShuiPer; //降水概率
    @SerializedName("wind_dir")
    public String windDirec;  //风向
    @SerializedName("wind_sc")
    public String windStrength; //风力
    @SerializedName("wind_spd")
    public String windSpeed; //风速
    @SerializedName("vis")
    public String viewDistance;  //能见度
    @SerializedName("sr")
    public String sunriseTime; //日出时间
    public String sunsetTime;   //日落时间

}
