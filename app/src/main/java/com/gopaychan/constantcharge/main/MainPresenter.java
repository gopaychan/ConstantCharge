package com.gopaychan.constantcharge.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.gopaychan.constantcharge.ChargeApplication;
import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.PerActivity;
import com.gopaychan.constantcharge.executor.ThreadExecutor;
import com.gopaychan.constantcharge.home.map.ChargeNavigationActivity;
import com.gopaychan.constantcharge.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType.BD09LL;
import static com.gopaychan.constantcharge.home.map.ChargeMapFragment.ROUTE_PLAN_NODE;
import static com.gopaychan.constantcharge.main.MainActivity.activityList;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
public class MainPresenter implements IMainContract.IPresenter {

    private IMainContract.MainView mMainView;
    private boolean hasInitSuccess = false;
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
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    String authinfo = "";

    private void initNavi() {
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//
//            if (!hasBasePhoneAuth()) {
//
//                this.requestPermissions(authBaseArr, authBaseRequestCode);
//                return;
//
//            }
//        }
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, mMainView.getActivity().getString(R.string.charge_map_TTS_APP_ID));
        BNaviSettingManager.setNaviSdkParam(bundle);
        BaiduNaviManager.getInstance().init(mMainView.getActivity(), Environment.getExternalStorageDirectory().getPath(), mMainView.getActivity().getPackageName(),
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        ThreadUtils.runOnMainThread(new Runnable() {

                            @Override
                            public void run() {
                                mMainView.showSnackbar(authinfo);
                            }
                        });
                    }

                    @Override
                    public void initSuccess() {
                        mMainView.showSnackbar("百度导航引擎初始化成功");
                        hasInitSuccess = true;
                    }

                    @Override
                    public void initStart() {
                        mMainView.showSnackbar("百度导航引擎初始化开始");
                    }

                    @Override
                    public void initFailed() {
                        mMainView.showSnackbar("百度导航引擎初始化失败");
                        hasInitSuccess = false;
                    }
                }, null /*mTTSCallback*/, null, null);
    }

    @Override
    public void routeplanToNavi(LatLng destinationLatLng, String description) {
        BDLocation location = ChargeApplication.getInstance().getCurLocation();
        if (location == null) return;
        if (!hasInitSuccess) {
            mMainView.showSnackbar("百度导航引擎还未初始化");
            return;
        }
        mMainView.showProgressBar();
        // 权限申请
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            // 保证导航功能完备
//            if (!hasCompletePhoneAuth()) {
//                if (!hasRequestComAuth) {
//                    hasRequestComAuth = true;
//                    this.requestPermissions(authComArr, authComRequestCode);
//                    return;
//                } else {
//                    Toast.makeText(BNDemoMainActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
        BNRoutePlanNode sNode = new BNRoutePlanNode(location.getLongitude(), location.getLatitude(), location.getStreet() + location.getStreetNumber(), null, BD09LL);
        BNRoutePlanNode eNode = new BNRoutePlanNode(destinationLatLng.longitude, destinationLatLng.latitude, description + "充电桩", null, BD09LL);
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManager.getInstance().launchNavigator(mMainView.getActivity(), list, 1, true, new DemoRoutePlanListener(sNode));
    }

    @Override
    public void onStart() {
        mLocationClient.start();
    }

    @Override
    public void onStop() {
        mLocationClient.stop();
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
        /*
         * 设置途径点以及resetEndNode会回调该接口
         */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("ChargeNavigationActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(mMainView.getActivity(), ChargeNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            mMainView.hideProgressBar();
            mMainView.getActivity().startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            mMainView.showSnackbar("算路失败");
            mMainView.hideProgressBar();
        }
    }

    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mListeners != null && mListeners.size() > 0) {
                ChargeApplication.getInstance().setCurLocation(location);
                for (MainActivity.OnLocationChangeListener listener : mListeners) {
                    listener.onChange(location);
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
