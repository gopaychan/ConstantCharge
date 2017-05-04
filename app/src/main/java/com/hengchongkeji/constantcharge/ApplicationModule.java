package com.hengchongkeji.constantcharge;

import android.app.Application;
import android.content.Context;

import com.hengchongkeji.constantcharge.executor.JobExecutor;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;

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

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }
}
