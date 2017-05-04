package com.hengchongkeji.constantcharge.main.home;

import com.baidu.location.BDLocation;
import com.hengchongkeji.constantcharge.base.IBasePresenter;
import com.hengchongkeji.constantcharge.base.IBaseView;

/**
 * Created by gopaychan on 2017/3/26.
 */

public interface IHomeContract {

    interface IView extends IBaseView<IPresenter> {
        void showProgressBar();

        void hideProgressBar(String city);

        void showMapFragment();

        void showListFragment();

        void showModeText(String mode);

        void showHorizontalProgressBar();

        void hideHorizontalProgressBar();

    }

    interface IPresenter extends IBasePresenter {
        void resetMode();

        void onLocationChange(BDLocation location);

        void showHorizontalProgressBar();

        void hideHorizontalProgressBar();
    }
}
