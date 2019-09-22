package com.bj.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/home/web")
public class WebAty extends Activity {

    private WebView wv_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_web);

        Log.i("lybj", getIntent().getStringExtra("msg"));
        String uri = getIntent().getStringExtra("uri");
        wv_web = findViewById(R.id.wv_web);
        wv_web.loadUrl(uri);
    }
}
