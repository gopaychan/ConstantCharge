package com.hengchongkeji.constantcharge;

import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.base.PerActivity;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.main.home.map.ChargeMapFragment;
import com.hengchongkeji.constantcharge.manager.RegisterActivity;

import dagger.Component;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = ActivityModule.class)
public interface ActivityComponent {

//    void inject(MainActivity activity);

    void inject(ChargeDetailActivity activity);

//    void inject(ChargeMapActivity activity);

    void inject(ChargeMapFragment fragment);

    void inject(RegisterActivity activity);

    void inject(IntroductionActivity activity);

    BaseActivity activity();

}
