package com.gopaychan.constantcharge.main;

import android.support.design.widget.Snackbar;

import com.baidu.mapapi.model.LatLng;
import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.IBasePresenter;
import com.gopaychan.constantcharge.base.IBaseView;
import com.gopaychan.constantcharge.utils.ThreadUtils;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */

public interface IMainContract {
    interface IView extends IBaseView<IPresenter> {
        void showFragment(int position);

        void showSnackbar(final String msg);

        void showProgressBar();

        void hideProgressBar();
    }


    interface IPresenter extends IBasePresenter {
        void onTabSelected(int position);

        void registerOnLocationChangeListener(MainActivity.OnLocationChangeListener listener);

        void unregisterOnLocationChangeListener(MainActivity.OnLocationChangeListener listener);

        void routeplanToNavi(LatLng destinationLatLng, String description);

        void onStart();

        void onStop();
    }

    class MainView implements IMainContract.IView {
        private MainActivity mActivity;

        @Inject
        MainView(BaseActivity activity) {
            mActivity = (MainActivity) activity;
        }

        public MainActivity getActivity() {
            return mActivity;
        }

        @Override
        public void showFragment(int position) {
            mActivity.mViewPager.setCurrentItem(position);
        }

        @Override
        public void showSnackbar(final String msg) {
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(mActivity.mViewPager, msg, Snackbar.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void showProgressBar() {
            mActivity.mHomePresenter.showHorizontalProgressBar();
        }

        @Override
        public void hideProgressBar() {
            mActivity.mHomePresenter.hideHorizontalProgressBar();
        }

        @Override
        public void setPresenter(IMainContract.IPresenter presenter) {

        }
    }
}
