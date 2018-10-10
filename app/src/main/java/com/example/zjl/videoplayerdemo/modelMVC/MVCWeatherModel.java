package com.example.zjl.videoplayerdemo.modelMVC;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/10/10 0010.
 */

public interface MVCWeatherModel {
    Disposable getWeatherInfo(MVCOnWeatherListener listener);
}
