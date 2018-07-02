package com.example.zjl.videoplayerdemo.bean;

/**
 * Created by Endless on 2017/7/19.
 */


public class HttpResponse<T> {
    private int status;
    private String msg;
    private T data;
    private boolean success;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}