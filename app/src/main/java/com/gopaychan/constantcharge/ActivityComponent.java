package com.gopaychan.constantcharge;

import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.PerActivity;
import com.gopaychan.constantcharge.charge.ChargeDetailActivity;
import com.gopaychan.constantcharge.main.MainActivity;

import dagger.Component;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(ChargeDetailActivity activity);

    BaseActivity activity();

}
