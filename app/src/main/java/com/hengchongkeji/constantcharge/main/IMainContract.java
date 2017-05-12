package com.hengchongkeji.constantcharge.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.base.IBasePresenter;
import com.hengchongkeji.constantcharge.base.IBaseView;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;

import javax.inject.Inject;

import static com.hengchongkeji.constantcharge.charge.ChargeDetailActivity.TO_CHARGE_DETAIL_ACTIVITY_ARGS;

/**
 * Created by gopaychan on 2017/3/26.
 */

public interface IMainContract {
    interface IView extends IBaseView<IPresenter> {
        void showFragment(int position);

        void showSnackbar(final String msg);

        void showChargeDetailActivity(Station mapMarkerInfo);

        void showProgressBar();

        void hideProgressBar();
    }


    interface IPresenter extends IBasePresenter {
        void onTabSelected(int position);

        void registerOnLocationChangeListener(MainActivity.OnLocationChangeListener listener);

        void unregisterOnLocationChangeListener(MainActivity.OnLocationChangeListener listener);

        void routePlanToNavi(LatLng destinationLatLng, String description);

        void onScanningResult(Intent data);

        void onStart();

        void onStop();

        void onLocationPermissionDenied();

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
        public void showChargeDetailActivity(Station mapMarkerInfo) {
            Intent intent = new Intent(getActivity(), ChargeDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(TO_CHARGE_DETAIL_ACTIVITY_ARGS, mapMarkerInfo);
            mActivity.startActivity(intent);
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
