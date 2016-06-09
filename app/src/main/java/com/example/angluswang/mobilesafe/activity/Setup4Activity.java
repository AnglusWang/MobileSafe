package com.example.angluswang.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private Button mPre, mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        init();

        buttonListener();
    }

    private void init() {
        mPre = (Button) findViewById(R.id.previous_setup4);
        mNext = (Button) findViewById(R.id.finish_setup4);
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        finish();

        //两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup4Activity.this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);

        //更新sp，表示已经设置过向导页面了
        mPref.edit().putBoolean("configed", true).commit();
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
