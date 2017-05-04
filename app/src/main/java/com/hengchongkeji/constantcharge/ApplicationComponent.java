package com.hengchongkeji.constantcharge;

import android.content.Context;

import com.hengchongkeji.constantcharge.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gopaychan on 2017/3/26.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context context();
    ThreadExecutor threadExecutor();
}
