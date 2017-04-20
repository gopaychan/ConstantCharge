package com.gopaychan.constantcharge.main;

import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.IBasePresenter;
import com.gopaychan.constantcharge.base.IBaseView;

import javax.inject.Inject;

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

    class MainView implements IMainContract.IView {
        private MainActivity mActivity;

        @Inject
        MainView(BaseActivity activity) {
            mActivity = (MainActivity) activity;
        }

        @Override
        public void showFragment(int position) {
            mActivity.mViewPager.setCurrentItem(position);
        }

        @Override
        public void setPresenter(IMainContract.IPresenter presenter) {

        }
    }
}
