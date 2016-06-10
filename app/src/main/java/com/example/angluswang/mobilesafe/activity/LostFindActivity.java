package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;

public class LostFindActivity extends Activity {

    private SharedPreferences mPrefs;

    private ImageView ivProtect;
    private TextView reSetting;
    private TextView tvSafePhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = mPrefs.getBoolean("configed", false);    //判断是否进入过设置向导

        if (configed) {
            setContentView(R.layout.activity_lost_find);

            //根据sp更新电话号码
            tvSafePhone = (TextView) findViewById(R.id.safe_phone);
            String phone = mPrefs.getString("safe_phone", "");
            tvSafePhone.setText(phone);

            // 根据sp更新保护锁
            ivProtect = (ImageView) findViewById(R.id.iv_protect);
            boolean protect = mPrefs.getBoolean("protect", false);
            if (protect) {
                ivProtect.setImageResource(R.drawable.lock);
            } else {
                ivProtect.setImageResource(R.drawable.unlock);
            }

        }else {
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }

        reSetting = (TextView) findViewById(R.id.re_enter);
        reSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
                finish();
            }
        });
    }
}
