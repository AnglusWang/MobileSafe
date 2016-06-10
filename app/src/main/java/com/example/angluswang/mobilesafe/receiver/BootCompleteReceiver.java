package com.example.angluswang.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by Jeson on 2016/6/9.
 *  用于监听手机开机重启的广播
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        //判断是否开启防盗，否则不进行sim变更通知等相关处理
        Boolean protect = pref.getBoolean("protect", false);
        if (protect) {

            String sim = pref.getString("sim", null);
            if (!TextUtils.isEmpty(sim)) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = tm.getSimSerialNumber();    //拿到当前Sim卡信息

                if (sim.equals(currentSim)) {
                    System.out.println("手机安全~");
                }else {
                    System.out.println("Sim卡变更，发送报警短信！！！");

                    String phone = pref.getString("safe_phone", "");  //读取安全号码

                    //调用Sim卡管理器
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null,
                            "Sim card is changed!!!", null, null);
                }

            }
        }
    }
}
