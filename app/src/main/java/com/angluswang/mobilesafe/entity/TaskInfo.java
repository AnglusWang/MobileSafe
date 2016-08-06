package com.angluswang.mobilesafe.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by AnglusWang on 2016/8/6.
 * 应用进程信息
 */

public class TaskInfo {

    private Drawable icon;
    private String packageName;
    private String appName;
    private long memorySize;

    private boolean userApp; // 是否是用户 App

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }
}
