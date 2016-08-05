package com.angluswang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.angluswang.mobilesafe.dao.AddressDao;

/**
 * Created by Jeson on 2016/7/28.
 * 监听去电的广播接受者
 */

public class OutCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();
        String address = AddressDao.getAddress(number);
        Toast.makeText(context, address,
                Toast.LENGTH_SHORT).show();
    }
}
