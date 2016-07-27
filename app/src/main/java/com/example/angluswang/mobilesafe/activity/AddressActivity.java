package com.example.angluswang.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
                } else {
                    Animation shake = AnimationUtils.loadAnimation(
                            AddressActivity.this, R.anim.shake);

//                    shake.setInterpolator(new Interpolator() {
//                        @Override
//                        public float getInterpolation(float input) {
//                            return 2 * input + 10;
//                        }
//                    });

                    etNumber.startAnimation(shake);
                    vibrate();
                }
            }
        });

        // 监听电话输入文本框
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(s.toString());
                tv_result.setText(address);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 手机震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500); // 震动 0.5秒

        //第二个参数，从第一个位置开始循环， -1 表示不循环
        // 等待1秒，震动2秒，等待1秒，震动2秒
//        vibrator.vibrate(new long[]{1000, 2000, 1000, 2000}, -1);
//        vibrator.cancel(); // 取消震动
    }
}
