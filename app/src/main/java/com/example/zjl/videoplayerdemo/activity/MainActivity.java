package com.example.zjl.videoplayerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zjl.videoplayerdemo.R;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.example.zjl.videoplayerdemo.model.VideoModel;
import com.example.zjl.videoplayerdemo.presenter.VideoPresenter;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import android.net.Uri;

public class MainActivity extends BaseActivity<VideoPresenter, VideoModel> implements VideoContract.View {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.request)
    Button request;
    @BindView(R.id.clear)
    Button clear;
    @BindView(R.id.download)
    Button download;
    @BindView(R.id.deleteVideo)
    Button deleteVideo;


    public static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//获得外部存储器的第一层的文件对象
    private File storageFile;
    private File outputImage;
    private Uri imageUri;


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        mPresenter.getVideoListDataRequest("V9LG4B3A0", 0);
        initFileStore();
        initListener();
    }

    private void initFileStore() {
        storageFile = new File(PATH.getAbsoluteFile() + File.separator + "testVideo");
        if (!storageFile.isDirectory()) {//创建目录
            storageFile.mkdirs();
        }
    }

    private void initListener() {
        request.setOnClickListener(v -> {
            mPresenter.getVideoListDataRequest("V9LG4B3A0", 1);
        });

        clear.setOnClickListener(v -> {
            text.setText("");
        });

        deleteVideo.setOnClickListener(v -> {
            File[] files = storageFile.listFiles();
            if (files.length==0) {
                ToastUtil.showShort("对应的视频文件夹下为空");
            }

            for (File file : files) {
                if (file.getName().equals("video.mp4")) {
                    file.delete();
                    ToastUtil.showShort("删除了文件 video.mp4");
                }
            }
        });

        download.setOnClickListener(v -> {
            outputImage = new File(storageFile, "video.mp4");
//            outputImage = new File(storageFile, "picture.jpg");

            startDownLoad();
        });
    }

    private void startDownLoad() {
        //下载的图片测试地址 http://cdn.llsapp.com/crm_test_1449051526097.jpg
        //下载的视频测试地址 http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4
        FileDownloader.getImpl()
                .create("http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4")
                .setPath(outputImage.toString())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int percent = (int) ((double) soFarBytes / (double) totalBytes * 100);
                        text.setText("(" + percent + "%" + ")");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        text.setText("(" + "100%" + ")");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        LogUtils.loge(e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        continueDownLoad(task);//如果存在了相同的任务，那么就继续下载
                    }
                }).start();

        //fixme 每次的imageUri都是新的，可能没能及时通知相册更新，多点击会有效果的（可改进）
        imageUri = Uri.fromFile(outputImage);
        Intent intentBc1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc1.setData(imageUri);
        this.sendBroadcast(intentBc1);
    }

    private void continueDownLoad(BaseDownloadTask task) {
        while (task.getSmallFileSoFarBytes() != task.getSmallFileTotalBytes()) {
            int percent = (int) ((double) task.getSmallFileSoFarBytes() / (double) task.getSmallFileTotalBytes() * 100);
            text.setText("(" + percent + "%" + ")");
        }
    }

    @Override
    public void returnGetVideoListData(TestBean videoDataList) {
        // http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4
        if (videoDataList.getV9LG4B3A0().size() > 0) {
            text.setText(videoDataList.getV9LG4B3A0().get(0).getMp4_url());

            JCVideoPlayerStandard jcVideoPlayerStandard = findViewById(R.id.videoplayer);
            boolean setUp = jcVideoPlayerStandard.setUp(
                    videoDataList.getV9LG4B3A0().get(0).getMp4_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    TextUtils.isEmpty(videoDataList.getV9LG4B3A0().get(0).getDescription()) ? videoDataList.getV9LG4B3A0().get(0).getTitle() + "" : videoDataList.getV9LG4B3A0().get(0).getDescription());
            if (setUp) {
                Glide.with(mContext).load(videoDataList.getV9LG4B3A0().get(0).getCover())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                        .crossFade().into(jcVideoPlayerStandard.thumbImageView);//显示视频缩略图
            }
        } else {
            LogUtils.loge("indexOutOfBoundsException----没有获取到视频");
        }

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
