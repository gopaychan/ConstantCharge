package com.gopaychan.constantcharge.main;


import com.gopaychan.constantcharge.base.PerActivity;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
public class MainPresenter implements IMainContract.IPresenter {

    private IMainContract.IView mMainView;

    @Inject
    public MainPresenter(IMainContract.IView view){
        mMainView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void onTabSelected(int position) {
        mMainView.showFragment(position);
    }
}