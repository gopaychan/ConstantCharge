package com.gopaychan.constantcharge;

import android.app.Application;

import com.gopaychan.constantcharge.common.MyAppExceptions;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeApplication extends Application{
    private static ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        Thread.setDefaultUncaughtExceptionHandler(MyAppExceptions
                .getAppExceptionHandler());
    }

    private void initializeInjector(){
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
