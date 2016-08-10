package com.angluswang.mobilesafe.activity.activity;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;

import com.angluswang.mobilesafe.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by AnglusWang on 2016/8/9.
 * 清理缓存垃圾 活动界面
 */

@ContentView(R.layout.activity_clean_cache)
public class CleanCacheActivity extends Activity {

    @ViewInject(R.id.lv_clean_cache)
    private ListView lvClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {

        PackageManager packageManager = getPackageManager();
        /**
         * 接收2个参数
         * 第一个参数接收一个包名
         * 第二个参数接收aidl的对象
         */
//		  * @hide
//		     */
//		    public abstract void getPackageSizeInfo(String packageName,
//		            IPackageStatsObserver observer);
//		packageManager.getPackageSizeInfo();

        //安装到手机上面所有的应用程序
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : installedPackages) {
            getCacheSize(packageInfo);
        }
    }

    /**
     * 获取 应用缓存的大小（通过反射实现）
     *
     * @param packageInfo
     */
    private void getCacheSize(PackageInfo packageInfo) {
        try {
            Class<?> aClass = getClassLoader().loadClass("PackageManager");
            //通过反射获取到当前的方法
            Method method = aClass.getDeclaredMethod("getPackageSizeInfo",
                    String.class, IPackageStatsObserver.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
