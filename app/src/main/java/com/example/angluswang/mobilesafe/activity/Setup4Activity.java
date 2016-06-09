package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;

public class Setup4Activity extends Activity {

    private Button finish;
    private Button pre;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        mPrefs = getSharedPreferences("config", MODE_PRIVATE);

        previousPage();
        finishSet();

    }

    /**
     * 完成设置向导
     */
    private void finishSet() {
        finish = (Button) findViewById(R.id.finish_setup4);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
                finish();

                //更新sp，表示已经设置过向导页面了
                mPrefs.edit().putBoolean("configed", true).commit();

                //两个界面切换的动画
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
            }
        });
    }

    /**
     * 返回上一页
     */
    private void previousPage() {
        pre = (Button) findViewById(R.id.previous_setup4);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup4Activity.this, Setup2Activity.class));
                finish();

                overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
            }
        });
    }

}
