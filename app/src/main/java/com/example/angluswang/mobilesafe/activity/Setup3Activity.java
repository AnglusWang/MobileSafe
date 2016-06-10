package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.angluswang.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {

    private Button mNext;
    private Button mPre;
    private EditText etPhone;
    private Button btnSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        init();
        buttonListener();

        String phone = mPref.getString("safe_phone", "");
        etPhone.setText(phone);
    }

    private void init() {
        mPre = (Button) findViewById(R.id.previous_setup3);
        mNext = (Button) findViewById(R.id.next_setup3);

        etPhone = (EditText) findViewById(R.id.et_phone);
        btnSelect = (Button) findViewById(R.id.select_contact);

    }

    @Override
    public void showNextPage() {

        String phone = etPhone.getText().toString().trim();// trim 可以过滤空格

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "安全号码不能为空!", Toast.LENGTH_SHORT).show();
            return ;
        }

        mPref.edit().putString("safe_phone", phone).commit();// 保存安全号码

        startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
        finish();

        //两个界面切换的动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
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

        //选择联系人按钮
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup3Activity.this, ContactActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", ""); // 替换-和空格

            etPhone.setText(phone); // 把电话号码设置给输入框
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
