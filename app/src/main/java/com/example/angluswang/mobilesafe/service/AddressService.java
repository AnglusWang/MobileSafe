package com.example.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.db.dao.AddressDao;

/**
 * Created by Jeson on 2016/7/27.
 * 归属地服务，用于显示来电归属地
 */

public class AddressService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 监听来电
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyListener listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); // 监听来电的状态
    }

    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: // 电话铃声响了
                    System.out.println("电话铃响。。。");
                    String address = AddressDao.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
