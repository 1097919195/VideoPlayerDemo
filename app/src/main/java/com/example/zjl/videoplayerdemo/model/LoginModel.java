package com.example.zjl.videoplayerdemo.model;


import com.example.zjl.videoplayerdemo.api.Api;
import com.example.zjl.videoplayerdemo.api.HostType;
import com.example.zjl.videoplayerdemo.bean.LoginTokenData;
import com.example.zjl.videoplayerdemo.bean.Person;
import com.example.zjl.videoplayerdemo.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class LoginModel implements LoginContract.Model{
    @Override
    public Observable<Person> getToken(String username, String password) {
        return Api.getDefault(HostType.QUALITY_DATA_TEST)
                .login(username,password)
                .compose(RxSchedulers.io_main());
    }
}
