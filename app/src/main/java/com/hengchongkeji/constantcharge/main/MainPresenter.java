package com.hengchongkeji.constantcharge.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.base.PerActivity;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
public class MainPresenter implements IMainContract.IPresenter {

    public IMainContract.MainView mMainView;
    private ThreadExecutor mThreadExecutor;
    private List<MainActivity.OnLocationChangeListener> mListeners;
    private LocationClient mLocationClient;
    private BDLocationListener mLocationListener;

    @Inject
    public MainPresenter(IMainContract.MainView view, Context context, ThreadExecutor threadExecutor) {
        mMainView = view;
        mThreadExecutor = threadExecutor;
        mListeners = new ArrayList<>();
        initLocation(context);
    }

    @Override
    public void start() {
        initNavi();
    }

    @Override
    public void onTabSelected(int position) {
        mMainView.showFragment(position);
    }

    @Override
    public void registerOnLocationChangeListener(MainActivity.OnLocationChangeListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterOnLocationChangeListener(MainActivity.OnLocationChangeListener listener) {

    }

    private void initLocation(Context context) {
        // 定位初始化
        mLocationClient = new LocationClient(context);
        mLocationListener = new LocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("GCJ02"); // 设置坐标类型 国测局加密的坐标
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void initNavi() {
        BaiduNaviManager.getInstance().initNavi(mMainView.getActivity(), new BaiduNaviManager.onInitResponse() {
            @Override
            public void onResponse(int result) {
                switch (result) {
                    case BaiduNaviManager.INIT_NAVI_FAIL:
                        mMainView.showSnackbar("百度导航引擎初始化失败");
                        break;
                    case BaiduNaviManager.INIT_NAVI_KEY_CHECK_FAIL:
                        mMainView.showSnackbar("key校验失败");
                        break;
                }
            }
        });
    }

    @Override
    public void routePlanToNavi(LatLng destinationLatLng, String description) {
        if (!BaiduNaviManager.hasPermission) {
            mMainView.showSnackbar("请同意权限申请后再进行导航");
            MainActivityPermissionsDispatcher.needsPermissionWithCheck(mMainView.getActivity());
            return;
        }
        BDLocation location = ChargeApplication.getInstance().getCurLocation();
        if (location == null) {
            mMainView.showSnackbar("没有定位到当前位置，请检查是否打开定位权限或走到空旷的地方再尝试定位");
            return;
        }
        BaiduNaviManager.getInstance().routePlanToNavi(mMainView.getActivity(), location, destinationLatLng, description, new BaiduNaviManager.onRoutePlanResponse() {
            @Override
            public void onResponse(int result) {
                switch (result) {
                    case PLAN_NO_INIT:
                        mMainView.showSnackbar("百度导航引擎还未初始化");
                        MainActivityPermissionsDispatcher.needsPermissionWithCheck(mMainView.getActivity());
                        break;
                    case PLAN_INIT_NOW:
                        mMainView.showSnackbar("百度导航引擎正在初始化");
                        break;
                    case PLAN_OPEN_BAIDU_FAIL:
                        mMainView.showSnackbar("无法打开百度地图，请检查是否安装了该app");
                        break;
                    case PLAN_START:
                        mMainView.showProgressBar();
                        break;
                    case PLAN_SUCCESS:
                        mMainView.hideProgressBar();
                        break;
                    case PLAN_FAIL:
                        mMainView.hideProgressBar();
                        mMainView.showSnackbar("算路失败");
                        break;
                }
            }
        });
    }

    @Override
    public void onScanningResult(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
            String result = bundle.getString(CodeUtils.RESULT_STRING);
            mMainView.showSnackbar("解析结果:" + result);
            mMainView.showChargeDetailActivity(new Station());
        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
            mMainView.showSnackbar("解析二维码失败");
        }
    }

    @Override
    public void onStart() {
        mLocationClient.start();
    }

    @Override
    public void onStop() {
        mLocationClient.stop();
    }

    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            String description = location.getLocTypeDescription();
            if (description != null && description.contains("failed")) {
                ChargeApplication.getInstance().setCurLocation(null);
                if (mListeners != null && mListeners.size() > 0) {
                    for (MainActivity.OnLocationChangeListener listener : mListeners) {
                        if(android.os.Build.VERSION.SDK_INT < 23){
                            listener.onFail("定位失败，请检查网络或gps是否打开，或是否授予软件定位权限");
                        }else {
                            listener.onFail("定位失败，请检查网络或gps是否打开");
                        }
                    }
                }
                return;
            }
            ChargeApplication.getInstance().setCurLocation(location);
            if (mListeners != null && mListeners.size() > 0) {
                for (MainActivity.OnLocationChangeListener listener : mListeners) {
                    listener.onChange(location);
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    public void onLocationPermissionDenied(){
        if (mListeners != null && mListeners.size() > 0) {
            for (MainActivity.OnLocationChangeListener listener : mListeners) {
                listener.onFail("定位失败，请打开权限");
            }
        }
    }
}
