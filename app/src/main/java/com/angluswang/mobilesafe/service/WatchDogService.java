package com.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by AnglusWang on 2016/8/9.
 * 看门狗服务
 */

public class WatchDogService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
