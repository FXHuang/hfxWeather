package com.fxhuang.hfxweather.util;

import android.text.TextUtils;

import com.fxhuang.hfxweather.db.City;
import com.fxhuang.hfxweather.db.County;
import com.fxhuang.hfxweather.db.Province;
import com.fxhuang.hfxweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Utility {
    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allprovinces = new JSONArray(response);
                for(int i=0;i<allprovinces.length();i++){
                    JSONObject provinceObject = allprovinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }return false;
    }

    //解析处理服务器返回的市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allcities = new JSONArray(response);
                for(int i=0;i<allcities.length();i++){
                    JSONObject cityObject = allcities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }return false;
    }

    //解析处理县级数据
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allcounties = new JSONArray(response);
                for(int i=0;i<allcounties.length();i++){
                    JSONObject countyObject = allcounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }return false;
    }

    //将返回的JSON数据解析成Weather实体类
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
