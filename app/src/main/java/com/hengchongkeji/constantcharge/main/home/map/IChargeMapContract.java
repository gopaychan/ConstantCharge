package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.hengchongkeji.constantcharge.base.IBasePresenter;
import com.hengchongkeji.constantcharge.base.IBaseView;
import com.hengchongkeji.constantcharge.data.entity.Station;

/**
 * Created by gopayChan on 2017/4/20.
 */

public interface IChargeMapContract {

    interface IView extends IBaseView<IPresenter> {
        void showSnackbar(String msg);

        void showChargeBalanceText(String balance);

        BaiduMap getBaiduMap();

        void showPopupWindow(Marker marker);

        void hidePopupWindow(Marker marker);

        void showMarker(Station mapMarkerInfo);

        Activity getActivity();

        void showMapView();

        void hideMapView();

    }

    interface IPresenter extends IBasePresenter {
        void onStart();

        void onStop();

        void onDestroy();

        void onMapPause();

        void onMapResume();

        void onLocationChange(BDLocation location);

        void onLocationFail(String msg);

        void location();

    }
}
