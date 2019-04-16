package com.example.zhang.tgvideoconver;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.zhang.tgvideoconver.utils.AndroidUtilities;

/**
 * Created by Administrator on 2019/3/14.
 */

public class ApplicationLoader extends Application {
    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        applicationHandler = new Handler(applicationContext.getMainLooper());



    }
}
