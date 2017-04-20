package com.gopaychan.constantcharge;

import android.content.Context;

import com.gopaychan.constantcharge.executor.ThreadExecutor;

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
