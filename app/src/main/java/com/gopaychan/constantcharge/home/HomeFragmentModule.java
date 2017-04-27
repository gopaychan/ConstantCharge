package com.gopaychan.constantcharge.home;

import com.gopaychan.constantcharge.base.PerActivity;
import com.gopaychan.constantcharge.home.map.IChargeMapContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gopayChan on 2017/4/25.
 */

@Module
public class HomeFragmentModule {
    private IChargeMapContract.IView mMapView;
    private IHomeContract.IView mHomeView;

    public HomeFragmentModule(IHomeContract.IView homeView, IChargeMapContract.IView mapView) {
        mMapView = mapView;
        mHomeView = homeView;
    }

    @Provides
    @PerActivity
    IChargeMapContract.IView mapView() {
        return mMapView;
    }

    @Provides
    @PerActivity
    IHomeContract.IView homeView() {
        return mHomeView;
    }
}
