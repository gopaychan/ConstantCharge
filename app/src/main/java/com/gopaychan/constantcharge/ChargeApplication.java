package com.gopaychan.constantcharge;

import android.app.Application;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeApplication extends Application{
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector(){
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
