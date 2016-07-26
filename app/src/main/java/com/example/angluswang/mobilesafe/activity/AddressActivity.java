package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.angluswang.mobilesafe.R;
import com.example.angluswang.mobilesafe.db.dao.AddressDao;

/**
 * 归属地查询界面
 */
public class AddressActivity extends Activity {

    private EditText etNumber;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        etNumber = (EditText) findViewById(R.id.et_number);
        tv_result = (TextView) findViewById(R.id.tv_result);

        Button query = (Button) findViewById(R.id.btn_address_query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //trim() 功能除去字符串开头和末尾的空格或其他字符。
                String number = etNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(number)) {
                    String address = AddressDao.getAddress(number);
                    tv_result.setText(address);
                }
            }
        });
    }
}
