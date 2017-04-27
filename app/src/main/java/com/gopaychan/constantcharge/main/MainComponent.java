package com.gopaychan.constantcharge.main;

import com.gopaychan.constantcharge.ActivityModule;
import com.gopaychan.constantcharge.ApplicationComponent;
import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.PerActivity;
import com.gopaychan.constantcharge.home.HomeFragmentModule;
import com.gopaychan.constantcharge.home.map.IChargeMapContract;

import dagger.Component;

/**
 * Created by gopayChan on 2017/4/25.
 */

@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class,HomeFragmentModule.class})
interface MainComponent {
    void inject(MainActivity activity);
    IChargeMapContract.IView view();
    BaseActivity activity();
}
