package com.example.zjl.videoplayerdemo.presenter;

import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.baserx.RxSubscriber2;

import java.util.List;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class VideoPresenter extends VideoContract.Presenter {
    @Override
    public void getVideoListDataRequest(String type, int startPage) {
        mRxManage.add(mModel.getVideoListData(type,startPage).subscribeWith(new RxSubscriber2<TestBean>(mContext,true) {
            @Override
            protected void _onNext(TestBean videoData) {
                mView.returnGetVideoListData(videoData);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
