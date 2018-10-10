package com.example.zjl.videoplayerdemo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zjl.videoplayerdemo.ICallback;
import com.example.zjl.videoplayerdemo.IRemoteService;
import com.example.zjl.videoplayerdemo.R;
import com.example.zjl.videoplayerdemo.app.AppConstant;
import com.example.zjl.videoplayerdemo.bean.Entity;
import com.example.zjl.videoplayerdemo.bean.HttpResponse;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;
import com.example.zjl.videoplayerdemo.bean.WeatherData;
import com.example.zjl.videoplayerdemo.contract.VideoContract;
import com.example.zjl.videoplayerdemo.model.VideoModel;
import com.example.zjl.videoplayerdemo.modelMVC.MVCOnWeatherListener;
import com.example.zjl.videoplayerdemo.modelMVC.MVCWeatherModel;
import com.example.zjl.videoplayerdemo.modelMVC.MVCWeatherModelImpl;
import com.example.zjl.videoplayerdemo.presenter.VideoPresenter;
import com.example.zjl.videoplayerdemo.service.MyService;
import com.google.gson.Gson;
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
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends BaseActivity<VideoPresenter, VideoModel> implements VideoContract.View, MVCOnWeatherListener {

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
    @BindView(R.id.upload)
    Button upload;


    public static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//获得外部存储器的第一层的文件对象
    private File storageFile;
    private File outputImage;
    private Uri imageUri;


    //包装的一层
//    private ICallback aidlService;
//
//    private IRemoteService interProcessCallback = new IRemoteService.Stub() {
//        @Override
//        public void doSomeThing(int anInt, String aString) throws RemoteException {
//
//        }
//
//        @Override
//        public void addEntity(Entity entity) throws RemoteException {
//
//        }
//
//        @Override
//        public List<Entity> getEntity() throws RemoteException {
//            MainActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                    tvRecvValue.setText(nVal+"");
//                }
//            });
//            return null;
//        }
//
//    };
//
//    private ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service)
//        {
//            aidlService =  ICallback.Stub.asInterface(service);
//            try {
//                //设置回调对象
//                aidlService.getReceive(interProcessCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name)
//        {
//            aidlService = null;
//        }
//    };

    //直接获取回调对象
    private IRemoteService s;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，获得 aidl定义的接口持有类
            s = IRemoteService.Stub.asInterface(service);
            Log.e("AIDL的service已经更新", "onServiceConnected service Qi");
            try {
                //设置回调对象
                List<Entity> entityList = s.getEntity();
                Log.e("AIDL的service已经更新", String.valueOf(entityList.size()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    //天气
    MVCWeatherModel weatherModel = new MVCWeatherModelImpl();
    /*管理Observables 和 Subscribers订阅,防止内存泄漏*/
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
        initAIDL();
    }

    //接受返回过来的实体类即实现通用需要向连接绑定服务
    private void initAIDL() {
        Intent mIntent = new Intent();
        mIntent.setAction("android.zjl.MyService");
        mIntent.setPackage("com.example.zjl.videoplayerdemo");
        bindService(mIntent, conn, BIND_AUTO_CREATE);
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
            addDataWithVideoPlayerProvider();
        });

        deleteVideo.setOnClickListener(v -> {
            File[] files = storageFile.listFiles();
            if (files.length == 0) {
                ToastUtil.showShort("对应的视频文件夹下为空");
                return;
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

        findViewById(R.id.download_from_server).setOnClickListener(v -> {
                    //不能向浏览器一样直接访问地址下载文件的
//            mPresenter.getDownloadDataRequest();
                    //调用库进行下载
                    FileDownloader.getImpl().create("http://192.168.199.163:8080/user/download").setWifiRequired(true).setPath(storageFile.toString() + File.separator + "android.apk").setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {

                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            Toast.makeText(MainActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {
                            continueDownLoad(task);//如果存在了相同的任务，那么就继续下载
                        }
                    }).start();
                }
        );

        upload.setOnClickListener(v -> {
            File[] files = storageFile.listFiles();
            if (files.length == 0) {
                ToastUtil.showShort("对应的视频文件夹下为空");
                return;
            }

            MultipartBody.Part[] images = new MultipartBody.Part[1];
            images[0] = getSpecialBodyTypePic("video.mp4");//注意文件的扩展名是不是需要声明

            for (File file : files) {
                if (file.getName().equals("video.mp4")) {
                    mPresenter.getUploadDataRequest("1", images);
                }
            }
        });

        findViewById(R.id.weatherBtn).setOnClickListener(v -> {
            Disposable disposable = weatherModel.getWeatherInfo(this);
            mCompositeDisposable.add(disposable);//再界面销毁的时候取消订阅
        });
    }

    private MultipartBody.Part getSpecialBodyTypePic(String filename) {
        File f = new File(PATH + File.separator + "testVideo" + File.separator + "video.mp4");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);//创建RequestBody，其中`multipart/form-data`为编码类型
        return MultipartBody.Part.createFormData("file", filename, requestFile);
    }

    // https://www.jianshu.com/p/601086916c8f
    private void addDataWithVideoPlayerProvider() {
        Uri bookUri = Uri.parse("content://com.zjl.contentproviderdemo.BookProvider/book");
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookName", "叫什么名字好呢");
        getContentResolver().insert(bookUri, contentValues);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "bookName"}, null, null, null);
        if (bookCursor != null) {
            while (bookCursor.moveToNext()) {
                Log.e("provider", "ID:" + bookCursor.getInt(bookCursor.getColumnIndex("_id"))
                        + "  BookName:" + bookCursor.getString(bookCursor.getColumnIndex("bookName")));
            }
            bookCursor.close();
        }

        Uri userUri = Uri.parse("content://com.zjl.contentproviderdemo.BookProvider/user");
        contentValues.clear();
        contentValues.put("userName", "叶叶叶");
        contentValues.put("sex", "男");
        getContentResolver().insert(userUri, contentValues);
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "userName", "sex"}, null, null, null);
        if (userCursor != null) {
            while (userCursor.moveToNext()) {
                Log.e("provider", "ID:" + userCursor.getInt(userCursor.getColumnIndex("_id"))
                        + "  UserName:" + userCursor.getString(userCursor.getColumnIndex("userName"))
                        + "  Sex:" + userCursor.getString(userCursor.getColumnIndex("sex")));
            }
            userCursor.close();
        }
    }

    private void startDownLoad() {
        //下载的图片测试地址 http://cdn.llsapp.com/crm_test_1449051526097.jpg
        //下载的视频测试地址 http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4
        FileDownloader.getImpl()
                .create("http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4")//下载apk需要服务端支持，避免解析失败
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

    //视频上传成功返回
    @Override
    public void returnUploadData(HttpResponse httpResponse) {
        ToastUtil.showShort(httpResponse.getMessage());
    }

    @Override
    public void returnDownloadData(HttpResponse httpResponse) {
        ToastUtil.showShort("下载好了");
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

    //MVC返回的WeatherData
    @Override
    public void returnWeatherDataSucceed(WeatherData weatherData) {
        LogUtils.loge(new Gson().toJson(weatherData));
    }

    @Override
    public void returnWeatherDataError(String meassage) {
        LogUtils.loge(meassage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
