package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;

public class Setup2Activity extends Activity {

    private Button next;
    private Button pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        //进入下一页
        next = (Button) findViewById(R.id.next_setup2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Setup2Activity.this, "进入下一页", Toast.LENGTH_SHORT).show();
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

    }
}
