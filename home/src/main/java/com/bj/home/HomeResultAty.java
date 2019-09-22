package com.bj.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/home/HomeResultAty")
public class HomeResultAty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_home_result);

        findViewById(R.id.btn_goback).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent in = new Intent();
                in.putExtra("msg", "从home模块返回的数据");
                setResult(999, in);
                finish();
            }
        });
    }
}
