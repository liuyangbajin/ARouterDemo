package com.bj.bs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 组件化架构方案
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_go_home).setOnClickListener(this);
        findViewById(R.id.btn_go_aty_forresult).setOnClickListener(this);
        findViewById(R.id.btn_get_frag).setOnClickListener(this);
        findViewById(R.id.btn_go_home_byArgs).setOnClickListener(this);
        findViewById(R.id.btn_test_interceptor).setOnClickListener(this);
        tv_msg = findViewById(R.id.tv_msg);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            // 跳转Activity页面
            case R.id.btn_go_home:

                ARouter.getInstance().build("/home/HomeAty").navigation();
                break;

            // 跳转Activity页面, 并且返回数据
            case R.id.btn_go_aty_forresult:

                ARouter.getInstance().build("/home/HomeResultAty").navigation(this, 897);
                break;

            // 获取fragment
            case R.id.btn_get_frag:

                Fragment mFragment = (Fragment) ARouter.getInstance().build("/home/HomeFragment").navigation();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl, mFragment).commit();
                break;

            // 携参数跳转
            case R.id.btn_go_home_byArgs:

                ARouter.getInstance().build("/home/arg")
                        .withString("msg", "5")
                        .withDouble("msg2", 6.0)
                        .navigation();
                break;

            // 拦截器测试
            case R.id.btn_test_interceptor:

                ARouter.getInstance().build("/home/web")
                        .withString("uri", "file:///android_asset/schame-test.html")
                        .navigation();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 897 && resultCode == 999){

            String msg = data.getStringExtra("msg");
            tv_msg.setText(msg);
        }
    }
}
