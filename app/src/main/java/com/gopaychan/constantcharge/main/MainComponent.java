package com.gopaychan.constantcharge.main;

import com.gopaychan.constantcharge.base.PerActivity;
import com.gopaychan.constantcharge.home.HomePresenterModule;

import dagger.Component;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
@Component(modules = {MainPresenterModule.class, HomePresenterModule.class})
public interface MainComponent {

    void inject(MainActivity activity);
}
