package com.gopaychan.constantcharge.home;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.gopaychan.constantcharge.R;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class HomePresenter implements IHomeContract.IPresenter {

    private IHomeContract.IView mView;

    public static final int HOME_MODE_MAP = 1;
    public static final int HOME_MODE_LIST = 2;
    private int mHomeMode = HOME_MODE_MAP;
    private Context mContext;

    @Inject
    public HomePresenter(IHomeContract.IView view, Context context) {
        mView = view;
        mContext = context;
    }

    @Inject
    public void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void resetMode() {
        if (mHomeMode == HOME_MODE_LIST) {
            mView.showMapFragment();
            mHomeMode = HOME_MODE_MAP;
            mView.showModeText(mContext.getString(R.string.home_mode_list));
        } else {
            mView.showListFragment();
            mHomeMode = HOME_MODE_LIST;
            mView.showModeText(mContext.getString(R.string.home_mode_map));
        }
    }

    @Override
    public void onLocationChange(BDLocation location) {
        if (location != null) {
            String city = location.getCity();
            city = city.contains("市") ? city.replace("市", "") : city;
            mView.hideProgressBar(city);
        }
    }

    @Override
    public void showHorizontalProgressBar() {
        mView.showHorizontalProgressBar();
    }

    @Override
    public void hideHorizontalProgressBar() {
        mView.hideHorizontalProgressBar();
    }
}
