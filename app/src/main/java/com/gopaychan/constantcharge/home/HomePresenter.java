package com.gopaychan.constantcharge.home;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class HomePresenter implements IHomeContract.IPresenter {

    private IHomeContract.IView mView;

    @Inject
    public HomePresenter(IHomeContract.IView view) {
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
