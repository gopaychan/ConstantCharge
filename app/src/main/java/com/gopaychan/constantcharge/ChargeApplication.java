package com.gopaychan.constantcharge;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.gopaychan.constantcharge.common.MyAppExceptions;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeApplication extends Application {
    private static ApplicationComponent mApplicationComponent;
    private BDLocation mLastLocation;
    private static ChargeApplication mInstance;

    public static ChargeApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        initializeInjector();
        Thread.setDefaultUncaughtExceptionHandler(MyAppExceptions
                .getAppExceptionHandler());
        mInstance = this;
//        if (BuildConfig.DEBUG) {
//            BlockCanary.install(this, new AppBlockCanaryContext()).start();
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//        }

    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setCurLocation(BDLocation location) {
        mLastLocation = location;
    }

    public boolean isLocationNull() {
        return mLastLocation == null;
    }

    public BDLocation getCurLocation(){
        return mLastLocation;
    }
}
