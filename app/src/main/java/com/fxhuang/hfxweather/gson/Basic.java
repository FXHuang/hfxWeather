package com.fxhuang.hfxweather.gson;

import com.google.gson.annotations.SerializedName;



public class Basic {
    @SerializedName("location")
    public String countyName;  //当前查询地区
    @SerializedName("cid")
    public String weatherId;   //当前查询地区代码
    @SerializedName("parent_city")
    public String cityName;    //当前查询地区所属上级城市
    @SerializedName("admin_area")  //当前查询地区所属行政区
    public String provinceName;
}
