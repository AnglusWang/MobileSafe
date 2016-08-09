package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by AnglusWang on 2016/8/9.
 * 应用程序锁 输入密码界面
 */
@ContentView(R.layout.activity_set_pwd)
public class EnterPwdActivity extends Activity
        implements View.OnClickListener {

    @ViewInject(R.id.et_pwd)
    private EditText et_pwd;
    @ViewInject(R.id.bt_0)
    private Button bt_0;
    @ViewInject(R.id.bt_1)
    private Button bt_1;
    @ViewInject(R.id.bt_2)
    private Button bt_2;
    @ViewInject(R.id.bt_3)
    private Button bt_3;
    @ViewInject(R.id.bt_4)
    private Button bt_4;
    @ViewInject(R.id.bt_5)
    private Button bt_5;
    @ViewInject(R.id.bt_6)
    private Button bt_6;
    @ViewInject(R.id.bt_7)
    private Button bt_7;
    @ViewInject(R.id.bt_8)
    private Button bt_8;
    @ViewInject(R.id.bt_9)
    private Button bt_9;
    @ViewInject(R.id.bt_clean_all)
    private Button bt_clean_all;
    @ViewInject(R.id.bt_delete)
    private Button bt_delete;
    @ViewInject(R.id.bt_ok)
    private Button bt_ok;

    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {

        et_pwd.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        if (intent != null) {
            packageName = intent.getStringExtra("packageName");
        }

        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);

        bt_clean_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pwd.setText("");
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {

            private String str;

            @Override
            public void onClick(View v) {
                str = et_pwd.getText().toString();
                if (str.length() == 0) {
                    return;
                }
                et_pwd.setText(str.substring(0, str.length() - 1));
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = et_pwd.getText().toString();
                if ("123".equals(result)) { // 此处密码已写死

                    Intent intent = new Intent();
                    intent.setAction("com.angluswang.mobilesafe.stopprotect");
                    // 停止保护短信
                    intent.putExtra("packageName", packageName);
                    sendBroadcast(intent); // 发送广播 停止保护

                    finish();
                } else {
                    UIUtils.showToast(EnterPwdActivity.this, "密码错误");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String str = et_pwd.getText().toString();
        switch (v.getId()) {
            case R.id.bt_0:
                et_pwd.setText(str + bt_0.getText().toString());
                break;
            case R.id.bt_1:
                et_pwd.setText(str + bt_1.getText().toString());
                break;
            case R.id.bt_2:
                et_pwd.setText(str + bt_2.getText().toString());
                break;
            case R.id.bt_3:
                et_pwd.setText(str + bt_3.getText().toString());
                break;
            case R.id.bt_4:
                et_pwd.setText(str + bt_4.getText().toString());
                break;
            case R.id.bt_5:
                et_pwd.setText(str + bt_5.getText().toString());
                break;
            case R.id.bt_6:
                et_pwd.setText(str + bt_6.getText().toString());
                break;
            case R.id.bt_7:
                et_pwd.setText(str + bt_7.getText().toString());
                break;
            case R.id.bt_8:
                et_pwd.setText(str + bt_8.getText().toString());
                break;
            case R.id.bt_9:
                et_pwd.setText(str + bt_9.getText().toString());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 当用户输入后退健的时候。进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }
}
