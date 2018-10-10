package com.example.zjl.videoplayerdemo.modelMVC;

import com.example.zjl.videoplayerdemo.api.Api;
import com.example.zjl.videoplayerdemo.api.HostType;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/10/9 0009.
 */

public class MVCWeatherModelImpl implements MVCWeatherModel{

    //disposable便是这个订阅事件  返回该对象可以再销毁界面时取消订阅
    @Override
    public Disposable getWeatherInfo(MVCOnWeatherListener listener) {
        return Api.getDefault(HostType.WEATHER_DATA)
                .getWeather("w3ghwrnxngne3zfa","hangzhou")
                .compose(RxSchedulers.io_main())
                .doOnSubscribe(disposable->
                        listener.showLoading("circleProgressBar"))
                .subscribe(weatherData-> {
                        listener.returnWeatherDataSucceed(weatherData);
                    }, message->{
                        listener.returnWeatherDataError(message.getMessage());
                    }
                );//使用subscribeWith显示对话框获取不到mContent即activity所以不能使用它来展示对话框
    }
}
