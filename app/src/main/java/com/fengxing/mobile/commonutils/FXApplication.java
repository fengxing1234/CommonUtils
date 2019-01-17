package com.fengxing.mobile.commonutils;

import android.app.Application;
import android.content.Context;

public class FXApplication extends Application {


    private Context mContext;
    private static FXApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mApplication = this;
    }


    public Context getContext() {
        return mContext;
    }

    public static FXApplication getFXApplication() {
        return mApplication;
    }

}
