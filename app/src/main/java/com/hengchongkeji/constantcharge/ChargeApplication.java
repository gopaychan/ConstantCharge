package com.hengchongkeji.constantcharge;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.hengchongkeji.constantcharge.common.MyAppExceptions;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeApplication extends Application {
    private static ApplicationComponent mApplicationComponent;
    private BDLocation mLastLocation;
    private static ChargeApplication mInstance;
    public static boolean isLogin = false;

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
        isLogin = PreferenceUtils.getLoginState(this);
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

    public void login() {
        isLogin = true;
        PreferenceUtils.setLoginState(this, isLogin);
    }

    public void logout() {
        isLogin = false;
        PreferenceUtils.setLoginState(this, isLogin);
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

    public BDLocation getCurLocation() {
        return mLastLocation;
    }
}
