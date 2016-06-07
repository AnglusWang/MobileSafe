package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.angluswang.mobilesafe.R;

public class LostFindActivity extends Activity {

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = mPrefs.getBoolean("configed", false);    //判断是否进入过设置向导

        if (configed) {
            setContentView(R.layout.activity_lost_find);
        }else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }
}
