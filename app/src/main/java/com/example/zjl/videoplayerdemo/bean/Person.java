package com.example.zjl.videoplayerdemo.bean;

/**
 * Created by Administrator on 2018/8/11 0011.
 */

public class Person {

    public Person(String name, String introduce, String id) {
        this.name = name;
        this.introduce = introduce;
        this.id = id;
    }

    /**
     * name : zjl
     * age :  23
     * id : 1097919195
     * image : https
     * status : 200
     * success : true
     * introduce : 人之初
     * label : 好人
     */


    private String name;
    private String age;
    private String id;
    private String image;
    private int status;
    private boolean success;
    private String introduce;
    private String label;
    private String loginToken;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
