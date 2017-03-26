package com.gopaychan.constantcharge.home;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class HomePresenter implements HomeContract.IPresenter {

    private HomeContract.IView mView;

    @Inject
    public HomePresenter(HomeContract.IView view) {
        mView = view;
    }


    @Inject
    public void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
