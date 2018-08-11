package com.example.zjl.videoplayerdemo.contract;

import com.example.zjl.videoplayerdemo.bean.LoginTokenData;
import com.example.zjl.videoplayerdemo.bean.Person;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import io.reactivex.Observable;


/**
 * Created by Administrator on 2018/6/1 0001.
 */

public interface LoginContract {
    interface Model extends BaseModel {
        Observable<Person> getToken(String username, String password);
    }

    interface View extends BaseView {
        void returnGetToken(Person person);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getTokenRequset(String username, String password);
    }
}
