package com.bj.bs.interceptor;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

@Interceptor(priority = 4)
public class LoginInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {

        String uri = postcard.getExtras().getString("uri");
        if(TextUtils.isEmpty(uri)){

            Log.i("lybj", "uri为空，中断路由");
            callback.onInterrupt(null);
        }else {

            Log.i("lybj", "拦截器执行，uri不为空，继续执行吧");
            postcard.withString("msg", "可以随意加内容");
            callback.onContinue(postcard);
        }
    }

    @Override
    public void init(Context context) {
    }
}