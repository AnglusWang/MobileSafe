package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        //重新进入向导设置
        TextView reEnter = (TextView) findViewById(R.id.re_enter);
        reEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
                finish();
            }
        });
    }
}
