package com.example.zjl.videoplayerdemo.contract;

import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public interface VideoContract {
    interface Model extends BaseModel{
        Observable<TestBean> getVideoListData(String type, int startPage);
    }

    interface View extends BaseView{
        void returnGetVideoListData(TestBean videoDataList);
    }

    abstract class Presenter extends BasePresenter<View,Model>{
        public abstract void getVideoListDataRequest(String type, int startPage);
    }
}
