package com.hengchongkeji.constantcharge.main;

import com.hengchongkeji.constantcharge.ActivityModule;
import com.hengchongkeji.constantcharge.ApplicationComponent;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.base.PerActivity;
import com.hengchongkeji.constantcharge.main.home.HomeFragmentModule;
import com.hengchongkeji.constantcharge.main.home.map.IChargeMapContract;

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
