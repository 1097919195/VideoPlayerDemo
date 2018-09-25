package com.example.zjl.videoplayerdemo.presenter;


import com.example.zjl.videoplayerdemo.bean.Person;
import com.example.zjl.videoplayerdemo.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSubscriber2;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void getTokenRequset(String username, String password) {
        mRxManage.add(mModel.getToken(username,password).subscribeWith(new RxSubscriber2<Person>(mContext, true) {
            @Override
            protected void _onNext(Person person) {
                mView.returnGetToken(person);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
