package com.fxhuang.hfxweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxhuang.hfxweather.gson.Forecast;
import com.fxhuang.hfxweather.gson.Lifestyle;
import com.fxhuang.hfxweather.gson.Weather;
import com.fxhuang.hfxweather.service.AutoUpdateService;
import com.fxhuang.hfxweather.util.HttpUtil;
import com.fxhuang.hfxweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.prefs.PreferenceChangeEvent;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private LinearLayout lifestyleLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView dressText;
    private TextView fluText;
    private TextView sportText;
    private TextView travelText;
    private TextView uvText;
    private TextView washCarText;
    private TextView airText;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //尝试沉浸状态栏（自动变色）,实测下面的代码会使状态栏一直为白色
        /*if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/

        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        setContentView(R.layout.activity_weather);
        //初始化各控件
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button)findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        lifestyleLayout = (LinearLayout)findViewById(R.id.lifestyle_layout);
       // aqiText = (TextView)findViewById(R.id.aqi_text);
       // pm25Text = (TextView)findViewById(R.id.pm25_text);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = pref.getString("weather",null);


        if(weatherString!=null){  //有缓存则直接解析
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        String bingPic = pref.getString("bing_pic",null);
        if(bingPic !=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }

    }

    public void requestWeather(final String weatherId){
        String weatherUrl = "https://free-api.heweather.com/s6/weather?key=94692190e4ab4289b04692ea0663cca1&location="+
                weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather !=null && "ok".equals(weather.status)){
                            //System.out.println(weather.toString());
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        loadBingPic();
    }

    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }


    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.countyName;
        String updateName = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText("更新于："+updateName);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView tempMaxText = (TextView)view.findViewById(R.id.tempMax_text);
            TextView tempMinText = (TextView)view.findViewById(R.id.tempMin_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.dayInfo);
            tempMaxText.setText(forecast.temperatureMax+"℃");
            tempMinText.setText(forecast.temperatureMin+"℃");
            forecastLayout.addView(view);
        }
       lifestyleLayout.removeAllViews();
        for (Lifestyle lifestyle:weather.lifestyleList){
            View view = LayoutInflater.from(this).inflate(R.layout.lifestyle_item,lifestyleLayout,false);
            TextView suggestionTypeText = (TextView)view.findViewById(R.id.suggestion_type_text);
            TextView suggestionIndexText = (TextView)view.findViewById(R.id.suggestion_index_text);
            TextView suggestionContentText = (TextView)view.findViewById(R.id.suggestion_content_text);
            //suggestionTypeText.setText(lifestyle.styleType);
            //生活指数类型 comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、
            // flu：感冒指数、sport：运动指数、trav：旅游指数、
            // uv：紫外线指数、air：空气污染扩散条件指数
            switch (lifestyle.styleType){
                case "comf":suggestionTypeText.setText("舒适度指数"); break;
                case "cw":suggestionTypeText.setText("洗车指数");break;
                case "drsg":suggestionTypeText.setText("穿衣指数");break;
                case "flu":suggestionTypeText.setText("感冒指数");break;
                case "sport":suggestionTypeText.setText("运动指数");break;
                case "trav":suggestionTypeText.setText("旅游指数");break;
                case "uv":suggestionTypeText.setText("紫外线指数");break;
                case "air":suggestionTypeText.setText("空气污染扩散条件指数");break;
            }
            suggestionIndexText.setText(lifestyle.feeling);
            suggestionContentText.setText(lifestyle.suggestion);
            lifestyleLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
        if(weather !=null&&"ok".equals(weather.status)){
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(WeatherActivity.this,"自动更新天气失败",Toast.LENGTH_SHORT).show();
        }
    }
}
