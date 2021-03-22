package com.example.myapplication.myapplication;

import android.app.Application;


public class MyApplication extends Application {

//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);



    }
//
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication leakApplication = (MyApplication) context.getApplicationContext();
//        return leakApplication.refWatcher;
//
//    }


}
