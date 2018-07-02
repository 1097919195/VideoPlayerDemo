package com.example.zjl.videoplayerdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.zjl.videoplayerdemo.R;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.example.zjl.videoplayerdemo.model.VideoModel;
import com.example.zjl.videoplayerdemo.presenter.VideoPresenter;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<VideoPresenter,VideoModel> implements VideoContract.View {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.request)
    Button request;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        mPresenter.getVideoListDataRequest("V9LG4B3A0",0);
        initListener();
    }

    private void initListener() {
        request.setOnClickListener(v -> {
            mPresenter.getVideoListDataRequest("V9LG4B3A0",1);
        });
    }

    @Override
    public void returnGetVideoListData(TestBean videoDataList) {
        text.setText(videoDataList.getV9LG4B3A0().get(0).getMp4_url());
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        ToastUtil.showShort(msg);
    }
}
