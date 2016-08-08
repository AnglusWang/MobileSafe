package com.angluswang.mobilesafe.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.angluswang.mobilesafe.R;
import com.angluswang.mobilesafe.dao.AppLockDao;
import com.angluswang.mobilesafe.engine.AppInfos;
import com.angluswang.mobilesafe.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static com.angluswang.mobilesafe.R.id.tv_unlock;

/**
 * Created by AnglusWang on 2016/8/8.
 * 为加锁的活动界面
 */

public class UnLockFragment extends Fragment {

    private View view;
    private ListView lvApps;
    private TextView tvUnlock;

    private ArrayList<AppInfo> unLockLists; // 未加锁的程序集合

    private AppLockDao dao;
    private UnClockAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_unlock_fragment, null);
        lvApps = (ListView) view.findViewById(R.id.lv_apps);
        tvUnlock = (TextView) view.findViewById(tv_unlock);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        List<AppInfo> appInfos = AppInfos.getAppInfos(getActivity());
        // 获取到程序锁的 dao
        dao = new AppLockDao(getActivity());
        unLockLists = new ArrayList<>();
        for (AppInfo appInfo : appInfos) {
            // 判断当前的应用是否在加锁程序数据库中
            if (dao.find(appInfo.getApkPackageName())) {

            } else {
                unLockLists.add(appInfo);
            }
        }

        adapter = new UnClockAdapter();
        lvApps.setAdapter(adapter);
    }

    private class UnClockAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            tvUnlock.setText("未加锁(" + unLockLists.size() + ")个");
            return unLockLists.size();
        }

        @Override
        public Object getItem(int position) {
            return unLockLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position,
                            View convertView, ViewGroup parent) {
            ViewHolder holder;
            final View view;
            final AppInfo appInfo;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_unlock, null);

                holder.imgIcon = (ImageView) view.findViewById(R.id.img_icon);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.imgUnlock = (ImageView) view.findViewById(R.id.img_unlock);

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            appInfo = unLockLists.get(position);

            holder.imgIcon.setImageDrawable(appInfo.getIcon());
            holder.tvName.setText(appInfo.getApkName());

            // 把程序添加到程序锁数据库里面
            holder.imgUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 初始化一个位移动画
                    TranslateAnimation translateAnimation =
                            new TranslateAnimation(
                                    Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, 1.0f,
                                    Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, 0);
                    // 设置动画时间
                    translateAnimation.setDuration(1000);
                    // 开始动画
                    view.startAnimation(translateAnimation);

                    new Thread() {
                        public void run() {
                            SystemClock.sleep(1000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 添加到数据库里面
                                    dao.add(appInfo.getApkPackageName());
                                    // 从当前的页面移除对象
                                    unLockLists.remove(position);
                                    // 刷新界面
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }.start();
                }
            });
            return view;
        }
    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView tvName;
        ImageView imgUnlock;
    }
}
