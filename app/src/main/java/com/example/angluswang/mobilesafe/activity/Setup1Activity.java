package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angluswang.mobilesafe.R;

public class Setup1Activity extends Activity {

    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

        next = (Button) findViewById(R.id.next_setup1);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setup1Activity.this, Setup2Activity.class));
                finish();

                //两个界面切换的动画
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
            }
        });

    }
}
