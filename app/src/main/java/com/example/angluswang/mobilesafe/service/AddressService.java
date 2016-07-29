package com.example.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.db.dao.AddressDao;

/**
 * Created by Jeson on 2016/7/27.
 * 归属地服务，用于显示来电归属地
 */

public class AddressService extends Service {

    private TelephonyManager tm;
    private MyListener listener;
    private OutCallReceiver outCallReceiver;
    private TextView view;
    private WindowManager mwm;
    private SharedPreferences mpref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mpref = getSharedPreferences("config", MODE_PRIVATE);

        // 监听来电
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); // 监听来电的状态

        outCallReceiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(outCallReceiver, filter); // 去电广播 注册
    }

    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: // 电话铃声响了
                    System.out.println("电话铃响。。。");
                    String address = AddressDao.getAddress(incomingNumber);
//                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE: // 电话闲置状态
                    if (mwm != null && view != null) {
                        mwm.removeView(view);
                        view = null;
                    }
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /**
     * 去电 广播监听
     */
    class OutCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(number);
//            Toast.makeText(context, address,
//                    Toast.LENGTH_SHORT).show();
            showToast(address);
        }
    }

    /**
     * 自定义归属地浮窗
     */
    public void showToast(String text) {
        mwm = (WindowManager)
                this.getSystemService(WINDOW_SERVICE); // 可以在第三方app中弹出自己的浮窗
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSPARENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");

//        view = new TextView(this);
//        view.setText(text);
//        view.setTextColor(Color.RED);
        View view = View.inflate(this, R.layout.toast_address, null);

        int[] bgs = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green};
        int style = mpref.getInt("address_style", 0);
        view.setBackgroundResource(bgs[style]); // 根据保存的风格样式，更新背景

        TextView tvText = (TextView) view.findViewById(R.id.tv_number);
        tvText.setText(text);
        mwm.addView(view, params); // 将View 添加到屏幕上 (Window)

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

        unregisterReceiver(outCallReceiver); // 取消注册
    }
}
