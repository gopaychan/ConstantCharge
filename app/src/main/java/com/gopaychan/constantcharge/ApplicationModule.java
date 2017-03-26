package com.gopaychan.constantcharge;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gopaychan on 2017/3/26.
 */

@Module
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides @Singleton Context provideApplicationContext(){
        return application;
    }
}