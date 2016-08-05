package com.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Jeson on 2016/6/9.
 * 防盗设置向导页面的基类
 */

public abstract class BaseSetupActivity extends Activity {

    private GestureDetector mDectector;
    public SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        // 手势识别器
        mDectector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            /**
             *
             * @param e1 表示滑动的起点
             * @param e2 表示滑动的终点
             * @param velocityX 表示滑动水平速度
             * @param velocityY 表示滑动垂直速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {

                // 判断纵向滑动幅度是否过大, 过大的话不允许切换界面
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    Toast.makeText(BaseSetupActivity.this, "不能这样划哦!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                // 判断滑动是否过慢
                if (Math.abs(velocityX) < 100) {
                    Toast.makeText(BaseSetupActivity.this, "滑动的太慢了!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                // 向右划, 进入上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPreviousPage();
                    return true;
                }

                // 向左划, 进入下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNextPage();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 展示下一页, 子类必须实现
     */
    public abstract void showNextPage();

    /**
     * 展示上一页, 子类必须实现
     */
    public abstract void showPreviousPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 委托手势识别器处理触摸事件
        mDectector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
