package com.example.zjl.videoplayerdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.zjl.videoplayerdemo.ICallback;
import com.example.zjl.videoplayerdemo.IRemoteService;
import com.example.zjl.videoplayerdemo.bean.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/4 0004.
 */

public class MyService extends Service {
    public static final String TAG = "MyService";
    IRemoteService callback;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, String.format("on bind,intent = %s", intent.toString()));
        return binder;
    }

    //用于包装了一层
//    private ICallback.Stub binderCallback = new ICallback.Stub() {
//        @Override
//        public void getReceive(IRemoteService service) throws RemoteException {
//            MyService.this.callback = callback;
//        }
//    };


    private final IRemoteService.Stub binder = new IRemoteService.Stub() {
        public static final String TAG = "IRemoteService.Stub";
        private List<Entity> data = new ArrayList<>();

        @Override
        public void doSomeThing(int anInt, String aString) throws RemoteException {
            Log.e(TAG, String.format("收到：%s, %s", anInt, aString));
        }

        @Override
        public void addEntity(Entity entity) throws RemoteException {
            Log.e(TAG, String.format("收到：entity = %s", entity));
            data.add(entity);
        }

        @Override
        public List<Entity> getEntity() throws RemoteException {
            return data;
        }

    };
}
