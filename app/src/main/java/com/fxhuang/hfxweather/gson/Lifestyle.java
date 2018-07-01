package com.fxhuang.hfxweather.gson;

import com.google.gson.annotations.SerializedName;



public class Lifestyle {
    @SerializedName("brf")
    public String feeling; //生活指数
    @SerializedName("txt")
    public String suggestion;
    @SerializedName("type")
    public String styleType; //生活指数类型 comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、
                             // flu：感冒指数、sport：运动指数、trav：旅游指数、
                                // uv：紫外线指数、air：空气污染扩散条件指数
}
