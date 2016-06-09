package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

    private Button next;
    private Button pre;
    private SettingItemView simUpdate;
    private SharedPreferences mPref;

    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        next();
        previous();
        bindSimListener();

        /**
         * 监听手势事件
         */
        mDetector = new GestureDetector(Setup2Activity.this,
                new GestureDetector.SimpleOnGestureListener(){
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

                        //向左滑动，切换下一页
                        if (e1.getRawX() - e2.getRawX() > 100) {
                            startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
                            finish();

                            //两个界面切换的动画
                            overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

                            return true;
                        }

                        //向右滑动，返回上一页
                        if (e2.getRawX() - e1.getRawX() > 100) {
                            startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
                            finish();

                            overridePendingTransition(R.anim.tran_previous_in,
                                    R.anim.tran_previous_out);

                            return true;
                        }

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //委托手势识别器处理触摸事件
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * Sim卡监听
     */
    private void bindSimListener() {
        simUpdate = (SettingItemView) findViewById(R.id.siv_sim);
        simUpdate.setChecked(false);
        simUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simUpdate.isChecked()) {

                    simUpdate.setChecked(false);
                    mPref.edit().putBoolean("sim_bind", false).commit();
                }else {

                    simUpdate.setChecked(true);
                    mPref.edit().putBoolean("sim_unbind", true).commit();
                }
            }
        });
    }

    /**
     * 返回上一页
     */
    private void previous() {
        pre = (Button) findViewById(R.id.previous_setup2);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
                finish();

                overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
            }
        });
    }

    /**
     * 进入下一页
     */
    private void next() {
        next = (Button) findViewById(R.id.next_setup2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
                finish();

                //两个界面切换的动画
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
            }
        });
    }
}
