package com.lishuaihua.albumdemo;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.tencent.bugly.Bugly;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Bugly.init(this, "bfb41af50a", BuildConfig.DEBUG);
    }
}
