package com.bj.home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/home/arg")
public class HomeByArgAty extends Activity {

    @Autowired(name = "msg")
    String arg1;

    @Autowired
    String arg2;

    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_home_arg);

        // 如果使用Autowired注解，需要加入底下的代码
        // 当然也可以用 getIntent().getStringExtra("")
        ARouter.getInstance().inject(this);

        tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setText("从主工程传递过来的参数："+arg1);
    }
}
