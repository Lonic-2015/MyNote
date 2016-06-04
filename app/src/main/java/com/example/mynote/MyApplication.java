package com.example.mynote;

import android.app.Application;
import android.content.Context;

/**
 * Created by 金晨 on 2016-06-03.
 */
public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
