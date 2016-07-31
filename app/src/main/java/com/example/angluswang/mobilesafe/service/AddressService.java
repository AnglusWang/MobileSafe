package com.example.angluswang.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
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
    private View view;
    private WindowManager mwm;
    private SharedPreferences mpref;
    private WindowManager.LayoutParams params;

    private int startX; //起始点坐标
    private int startY;

    private int winWidth; // 屏幕宽高
    private int winHight;

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
        // 获取屏幕宽高
        final Point point = new Point();
        mwm.getDefaultDisplay().getSize(point);
        winWidth = point.x;
        winHight = point.y;

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  没有触摸事件
        params.format = PixelFormat.TRANSPARENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP; //将重心位置设置为左上方
        params.setTitle("Toast");

        // WindowsManager 层归属地位置设定
        int lastX = mpref.getInt("lastX", 0);
        int lastY = mpref.getInt("lastY", 400);

        params.x = lastX; // 基于左上方的偏移量
        params.y = lastY;

//        view = new TextView(this);
//        view.setText(text);
//        view.setTextColor(Color.RED);
        view = View.inflate(this, R.layout.toast_address, null);

        int[] bgs = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green};
        int style = mpref.getInt("address_style", 0);
        view.setBackgroundResource(bgs[style]); // 根据保存的风格样式，更新背景

        // 设置触摸事件，实现可拖拽效果
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dx = endX - startX;
                        int dy = endY - startY;

                        // 更新浮窗的位置
                        params.x += dx;
                        params.y += dy;
                        // 防止坐标偏离屏幕
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > winWidth - view.getWidth()) {
                            params.x = winWidth - view.getWidth();
                        }
                        if (params.y > winHight - view.getHeight()) {
                            params.y = winHight - view.getHeight();
                        }

                        mwm.updateViewLayout(view, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mpref.edit().putInt("lastX", params.x)
                                .putInt("lastY", params.y).apply();
                        break;
                }
                return true;
            }
        });

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
