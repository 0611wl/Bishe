package com.example.administrator.bishe;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * @author mao
 * @version v1.0
 * @date 2017/5/10 17:36
 * @des 应用的Application初始化X5
 * @link http://blog.csdn.net/github_2011/article/details/71600734
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initTBS();
    }

    /**
     * 初始化TBS浏览服务X5内核
     */
    private void initTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.setDownloadWithoutWifi(true);//非wifi条件下允许下载X5内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {}
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
