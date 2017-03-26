package com.gopaychan.constantcharge.home;


import dagger.Module;
import dagger.Provides;

/**
 * Created by gopaychan on 2017/3/26.
 */

@Module
public class HomePresenterModule {
    private HomeContract.IView mView;

    public HomePresenterModule(HomeContract.IView view) {
        mView = view;
    }

    @Provides HomeContract.IView provideHomeContractView(){
        return mView;
    }
}
