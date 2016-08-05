package com.angluswang.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.angluswang.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private Button mPre, mNext;
    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        init();

        buttonListener();

        boolean protect = mPref.getBoolean("protect", false);
        // 根据sp保存的状态,更新checkbox
        if (protect) {
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        } else {
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }

        // 监听复选框
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect", true).commit();
                } else {
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect", false).commit();
                }
            }
        });
    }

    private void init() {
        mPre = (Button) findViewById(R.id.previous_setup4);
        mNext = (Button) findViewById(R.id.finish_setup4);

        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        finish();

        //两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

        //更新sp，表示已经设置过向导页面了
        mPref.edit().putBoolean("configed", true).commit();
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    private void buttonListener() {
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

}
