package com.bob.volleysample.bean;

/**
 * com.bob.volleysample.bean
 * Created by BOB on 2017/2/11.
 * 描述：Weather实体类
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class Weather {

    //此处的weatherinfo不可以修改为weatherInfo，因为从gson获取的数据格式为
    // {"weatherinfo":{"city":"北京","cityid":"101010100","temp":"19","WD":"南风","WS":"2级","SD":"43%","WSE":"2","time":"19:45","isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
    private WeatherInfo weatherinfo;

    public WeatherInfo getWeatherInfo() {
        return weatherinfo;
    }

    public void setWeatherInfo(WeatherInfo weatherinfo) {
        this.weatherinfo = weatherinfo;
    }
}
