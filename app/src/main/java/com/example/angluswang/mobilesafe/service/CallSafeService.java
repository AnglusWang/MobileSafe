package com.example.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.angluswang.mobilesafe.db.dao.BlackNumberDao;

/**
 * Created by Jeson on 2016/8/2.
 * 短信拦截服务
 */
public class CallSafeService extends Service {

    private BlackNumberDao dao;
    private InnerReceiver innerReceiver;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dao = new BlackNumberDao(this);
        //初始化短信的广播
        innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("sms:", "短信来了");

            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects) {// 短信最多140字节,
                // 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();// 短信来源号码
                String messageBody = message.getMessageBody();// 短信内容

                //通过短信的电话号码查询拦截的模式
                String mode = dao.findNumber(originatingAddress);
                /**
                 * 黑名单拦截模式
                 * 1 全部拦截 电话拦截 + 短信拦截
                 * 2 电话拦截
                 * 3 短信拦截
                 */
                if (mode.equals("1")) {
                    abortBroadcast();
                } else if (mode.equals("3")) {
                    abortBroadcast();
                }
                //智能拦截模式 发票
                if (messageBody.contains("fapiao")) {
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(innerReceiver);
        super.onDestroy();
    }
}
