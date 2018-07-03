package com.example.zjl.videoplayerdemo.app;


import android.content.Context;


import com.example.zjl.videoplayerdemo.BuildConfig;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.liulishuo.filedownloader.FileDownloader;


import io.reactivex.plugins.RxJavaPlugins;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger,注意拷贝的话BuildConfig.LOG_DEBUG一定要是在当前module下的包名，配置文件中判断测适和发行版本
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        setRxJavaErrorHandler();
        FileDownloader.setup(this);//注意作者已经不建议使用init方法
    }

//    /**
//     * 初始化RxJava回收执行的周期
//     * @see io.reactivex.internal.schedulers.SchedulerPoolFactory
//     */
//    private static void initRxPurgeProperties() {
//        System.setProperty(PURGE_ENABLED_KEY, "true");
//        System.setProperty(PURGE_PERIOD_SECONDS_KEY, "3600");
//    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> LogUtils.loge("throw test RxJava2===="+throwable.getMessage()));
    }
}
