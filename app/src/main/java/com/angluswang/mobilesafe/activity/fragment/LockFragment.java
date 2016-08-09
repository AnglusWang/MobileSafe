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

import static com.angluswang.mobilesafe.R.id.tv_lock;

/**
 * Created by AnglusWang on 2016/8/8.
 * 加锁的活动界面
 */

public class LockFragment extends Fragment {

    private ListView lvApps;
    private TextView tvLock;

    private List<AppInfo> lockLists; // 加锁的程序集合

    private AppLockDao dao;
    private LockAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_lock_fragment, null);
        lvApps = (ListView) view.findViewById(R.id.lv_apps);
        tvLock = (TextView) view.findViewById(tv_lock);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //拿到所有的应用程序
        List<AppInfo> appInfos = AppInfos.getAppInfos(getActivity());
        lockLists = new ArrayList<>();
        dao = new AppLockDao(getActivity());
        for (AppInfo appInfo : appInfos) {
            //如果能找到当前的包名说明在程序锁的数据库里面
            if (dao.find(appInfo.getApkPackageName())) {
                lockLists.add(appInfo);
            } else {

            }
        }
        adapter = new LockAdapter();
        lvApps.setAdapter(adapter);
    }

    private class LockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            tvLock.setText("已加锁(" + lockLists.size() + ")个");
            return lockLists.size();
        }

        @Override
        public Object getItem(int position) {
            return lockLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position,
                            View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final View view;
            if (convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(getActivity(), R.layout.item_lock, null);

                holder.imgIcon = (ImageView) view.findViewById(R.id.img_icon);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.imgLock = (ImageView) view.findViewById(R.id.img_lock);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            final AppInfo appInfo = lockLists.get(position);

            holder.imgIcon.setImageDrawable(appInfo.getIcon());
            holder.tvName.setText(appInfo.getApkName());

            holder.imgLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TranslateAnimation translateAnimation =
                            new TranslateAnimation(
                                    Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, -1.0f,
                                    Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, 0);
                    translateAnimation.setDuration(1000);
                    view.startAnimation(translateAnimation);

                    new Thread() {
                        public void run() {
                            SystemClock.sleep(1000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dao.delete(appInfo.getApkPackageName());
                                    lockLists.remove(position);
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
        ImageView imgLock;
    }
}
