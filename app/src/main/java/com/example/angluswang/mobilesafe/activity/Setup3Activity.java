package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;

public class Setup3Activity extends Activity {

    private Button next;
    private Button pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        //进入下一页
        next = (Button) findViewById(R.id.next_setup3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
                finish();

                //两个界面切换的动画
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
            }
        });

        //返回上一页
        pre = (Button) findViewById(R.id.previous_setup3);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
                finish();

                overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
            }
        });

    }
}
