// ICallback.aidl
package com.example.zjl.videoplayerdemo;
import com.example.zjl.videoplayerdemo.IRemoteService;

// Declare any non-default types here with import statements

interface ICallback {
    void getReceive(IRemoteService service);
}
