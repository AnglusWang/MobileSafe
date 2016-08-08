package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {

    private GridView gvHome;

    private String[] mItems = new String[] {"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

    private int[] mPics = new int[] {R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings };

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());

        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //手机防盗
                        showPasswordDailog();
                        break;
                    case 1:
                        // 通讯卫士：
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;
                    case 2:
                        // 软件管理
                        startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
                        break;
                    case 3:
                        // 进程管理
                        startActivity(new Intent(HomeActivity.this, TaskManagerActivity.class));
                        break;
                    case 5:
                        // 病毒查杀
                        startActivity(new Intent(HomeActivity.this, AntivirusActivity.class));
                        break;
                    case 7:
                        //高级工具
                        startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;
                    case 8:
                        //设置中心
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示密码弹窗
     */
    private void showPasswordDailog() {
        //判断是否设置过密码
        String savePassword = mPref.getString("password", null);
        if (!TextUtils.isEmpty(savePassword)) {
            //输入密码
            showPasswordInputDailog();
        }else {
            //如果没设置就弹出设置密码弹窗
            showPasswordSetDailog();
        }
    }

    /**
     * 显示输入密码弹窗
     */
    private void showPasswordInputDailog() {

        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_input_password, null);
//        alertDialog.setView(view);  //将自定义的布局文件设置给dailog
        dialog.setView(view, 0, 0, 0, 0);  //设置边距为零，保证在2.x版本上运行没问题

        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(password)) {
                    String savedPassword = mPref.getString("password", null);

                    if (MD5Utils.encode(password).equals(savedPassword)) {
//                        Toast.makeText(HomeActivity.this, "登录成功!",
//                                 Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // 跳转到手机防盗页
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));

                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();   // 隐藏dialog
            }
        });

        dialog.show();
    }

    /**
     * 显示设置密码弹窗
     */
    private void showPasswordSetDailog() {

        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_set_password, null);
//        alertDialog.setView(view);  //将自定义的布局文件设置给dailog
        dialog.setView(view, 0, 0, 0, 0);  //设置边距为零，保证在2.x版本上运行没问题

        final EditText etPassword = (EditText) view.
                findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.
                findViewById(R.id.et_password_confirm);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                // password!=null && !password.equals("")
                if (!TextUtils.isEmpty(password) && !passwordConfirm.isEmpty()) {
                    if (password.equals(passwordConfirm)) {
//                         Toast.makeText(HomeActivity.this, "登录成功!",
//                                 Toast.LENGTH_SHORT).show();

                        // 将密码保存起来
                         mPref.edit().putString("password",
                                 MD5Utils.encode(password)).commit();

                        dialog.dismiss();

                        // 跳转到手机防盗页
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));

                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();   // 隐藏dialog
            }
        });

        dialog.show();
    }

    private class HomeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);

            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            ivItem.setImageResource(mPics[position]);  tvItem.setText(mItems[position]);

            return view;
        }
    }
}
