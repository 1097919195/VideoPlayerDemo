package com.example.zjl.videoplayerdemo.modelMVC;

import com.example.zjl.videoplayerdemo.bean.WeatherData;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public interface MVCOnWeatherListener {
    void showLoading(String title);
    void returnWeatherDataSucceed(WeatherData weatherData);
    void returnWeatherDataError(String message);
}
