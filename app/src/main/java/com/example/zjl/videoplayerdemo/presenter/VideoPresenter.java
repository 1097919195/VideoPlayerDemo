package com.example.zjl.videoplayerdemo.presenter;

import com.example.zjl.videoplayerdemo.bean.HttpResponse;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.baserx.RxSubscriber2;

import java.util.List;

import okhttp3.MultipartBody;

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

    @Override
    public void getUploadDataRequest(String id, MultipartBody.Part[] images) {
        mRxManage.add(mModel.uploadData(id,images).subscribeWith(new RxSubscriber2<HttpResponse>(mContext, true) {
            @Override
            protected void _onNext(HttpResponse httpResponse) {
                mView.returnUploadData(httpResponse);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void getDownloadDataRequest() {
        mRxManage.add(mModel.downloadData().subscribeWith(new RxSubscriber2<HttpResponse>(mContext, true) {
            @Override
            protected void _onNext(HttpResponse httpResponse) {
                mView.returnDownloadData(httpResponse);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
