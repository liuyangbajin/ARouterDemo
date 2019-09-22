package com.bj.home;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * 首页模块
 *
 * 其中 path 是跳转的路径，这里的路径需要注意的是至少需要有两级，/xx/xx
 * */
@Route(path = "/home/HomeAty")
public class HomeAty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_home);
    }
}
