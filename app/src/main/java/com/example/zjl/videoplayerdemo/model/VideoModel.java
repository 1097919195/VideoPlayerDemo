package com.example.zjl.videoplayerdemo.model;

import com.example.zjl.videoplayerdemo.api.Api;
import com.example.zjl.videoplayerdemo.api.HostType;
import com.example.zjl.videoplayerdemo.bean.HttpResponse;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.TimeUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class VideoModel implements VideoContract.Model {
    @Override
    public Observable<TestBean> getVideoListData(final String type, int startPage) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO)
                .getVideoList(Api.getCacheControl(),type,startPage)
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<HttpResponse> uploadData(String id, MultipartBody.Part[] images) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("id", RequestBody.create(null, id));
        return Api.getDefault(HostType.BOOT_DATA)
                .uploadData(map,images)
                .compose(RxSchedulers.io_main());
    }

    @Override
    public Observable<HttpResponse> downloadData() {
        return Api.getDefault(HostType.BOOT_DATA)
                .download()
                .compose(RxSchedulers.io_main());
    }
}
