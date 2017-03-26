package com.gopaychan.constantcharge.main;

import com.gopaychan.constantcharge.base.IBasePresenter;
import com.gopaychan.constantcharge.base.IBaseView;

/**
 * Created by gopaychan on 2017/3/26.
 */

public interface IMainContract{
    interface IView extends IBaseView<IPresenter> {
        void showFragment(int position);
    }

    interface IPresenter extends IBasePresenter {
        void onTabSelected(int position);
    }
}
