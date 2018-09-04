// IRemoteService.aidl
package com.example.zjl.videoplayerdemo;
import com.example.zjl.videoplayerdemo.bean.Entity;

// Declare any non-default types here with import statements

interface IRemoteService {

    void doSomeThing(int anInt,String aString);

    void addEntity(in Entity entity);

    List<Entity> getEntity();
}
