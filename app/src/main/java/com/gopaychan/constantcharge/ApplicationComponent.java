package com.gopaychan.constantcharge;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by gopaychan on 2017/3/26.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context context();
}
