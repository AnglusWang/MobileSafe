package com.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by AnglusWang on 2016/8/7.
 * 自动清理进程服务
 */

public class KillProcessService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
