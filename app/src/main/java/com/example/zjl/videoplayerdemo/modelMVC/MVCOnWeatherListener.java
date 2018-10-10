package com.example.zjl.videoplayerdemo.modelMVC;

import com.example.zjl.videoplayerdemo.bean.WeatherData;
import com.jaydenxiao.common.base.BaseView;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public interface MVCOnWeatherListener extends BaseView {
    void returnWeatherDataSucceed(WeatherData weatherData);
    void returnWeatherDataError(String message);//其实可以直接使用BaseView中的showErrorTip统一封装
}
