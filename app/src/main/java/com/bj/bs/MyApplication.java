package com.bj.bs;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

public class MyApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if(isDebug()) {

            // 打印日志
            ARouter.openLog();

            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 初始化ARouter
        ARouter.init(this);
    }

    private boolean isDebug() {

        return BuildConfig.DEBUG;
    }
}
