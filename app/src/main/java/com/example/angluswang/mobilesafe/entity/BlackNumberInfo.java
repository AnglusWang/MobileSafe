package com.example.angluswang.mobilesafe.entity;

/**
 * Created by Jeson on 2016/8/1.
 * 黑名单实体类
 */

public class BlackNumberInfo {
    private String number;
    private String mode; // 模式有：1.拦截短信 2.拦截电话 3.全部拦截

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
