package com.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class KillProcesWidgetService extends Service {

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
