package com.hengchongkeji.constantcharge.main;

import com.hengchongkeji.constantcharge.ActivityModule;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.main.home.IHomeContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gopaychan on 2017/3/26.
 */

@Module
public class MainPresenterModule extends ActivityModule {

    private IMainContract.IView mMainView;
    private IHomeContract.IView mHomeView;

    public MainPresenterModule(IMainContract.IView view, IHomeContract.IView homeView) {
        super((BaseActivity) view);
        mMainView = view;
        mHomeView = homeView;
    }

    @Provides
    IMainContract.IView provideMainContractView() {
        return mMainView;
    }

    @Provides
    IHomeContract.IView provideHomeContractView() {
        return mHomeView;
    }
}
