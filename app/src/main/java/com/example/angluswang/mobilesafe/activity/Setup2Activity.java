package com.example.angluswang.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView simUpdate;

    private Button mPre, mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        init();
        buttonListener();
        bindSimListener();

    }

    /**
     * 初始化数据
     */
    private void init() {
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        mPre = (Button) findViewById(R.id.previous_setup2);
        mNext = (Button) findViewById(R.id.next_setup2);

        simUpdate = (SettingItemView) findViewById(R.id.siv_sim);
        simUpdate.setChecked(false);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
        finish();

        //两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();

        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    /**
     * Button按钮监听
     */
    public void buttonListener() {
        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousPage();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextPage();
            }
        });
    }

    /**
     * Sim卡是否绑定监听
     */
    private void bindSimListener() {

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
