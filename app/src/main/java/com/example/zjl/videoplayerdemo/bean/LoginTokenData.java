package com.example.zjl.videoplayerdemo.bean;

/**
 * Created by Administrator on 2018/8/11 0011.
 */

public class LoginTokenData {

    /**
     * name : zjl
     * age : 23
     * id : 1097919195
     * image : https
     * status : 200
     * success : true
     */

    private String name;
    private int age;
    private int id;
    private String image;
    private int status;
    private boolean success;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
