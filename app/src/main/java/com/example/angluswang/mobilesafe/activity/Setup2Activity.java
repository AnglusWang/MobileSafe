package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

    private Button next;
    private Button pre;
    private SettingItemView simUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        //进入下一页
        next = (Button) findViewById(R.id.next_setup2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
                finish();
            }
        });

        //返回上一页
        pre = (Button) findViewById(R.id.previous_setup2);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
                finish();
            }
        });

        //绑定sim卡监听
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
}
