package com.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.angluswang.mobilesafe.dao.BlackNumberDao;

import java.lang.reflect.Method;

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

        // 获得系统的电话服务
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyPhoneStateListener listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        //初始化短信的广播
        innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }


    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            /**
             * @see TelephonyManager#CALL_STATE_IDLE 呼叫状态闲置
             * @see TelephonyManager#CALL_STATE_RINGING 电话响铃状态
             * @see TelephonyManager#CALL_STATE_OFFHOOK 电话接通状态
             */
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:  //电话铃响的状态
                    /**
                     * 黑名单拦截模式
                     * 1 全部拦截 电话拦截 + 短信拦截
                     * 2 电话拦截
                     * 3 短信拦截
                     */
                    String mode = dao.findNumber(incomingNumber);
                    if (mode.equals("1") || mode.equals("2")) {

                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().
                                registerContentObserver(uri, true,
                                        new MyContentObserver(new Handler(), incomingNumber));

                        // 挂断电话
                        endCall();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class MyContentObserver extends ContentObserver {

        String incomingNumber; // 来电号码

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        // 当数据改变时调用的方法
        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber); // 删除电话记录

            super.onChange(selfChange);
        }
    }

    /**
     * 删除电话记录
     */
    private void deleteCallLog(String incommingNumber) {

        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri, "number=?", new String[]{incommingNumber});
    }

    /**
     * 挂断电话
     */
    private void endCall() {
        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
