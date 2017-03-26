package com.gopaychan.constantcharge.main;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gopaychan on 2017/3/26.
 */

@Module
public class MainPresenterModule {

    private IMainContract.IView mMainView;

    public MainPresenterModule(IMainContract.IView view){
        mMainView = view;
    }

    @Provides IMainContract.IView provideMainContractView(){
        return mMainView;
    }
}
