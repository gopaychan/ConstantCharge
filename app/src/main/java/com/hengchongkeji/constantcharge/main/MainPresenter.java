package com.hengchongkeji.constantcharge.main;


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
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.PerActivity;
import com.hengchongkeji.constantcharge.data.domain.MapMarkerInfo;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.main.home.map.ChargeNavigationActivity;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType.BD09LL;
import static com.hengchongkeji.constantcharge.main.MainActivity.activityList;
import static com.hengchongkeji.constantcharge.main.home.map.ChargeMapFragment.ROUTE_PLAN_NODE;

/**
 * Created by gopaychan on 2017/3/26.
 */
@PerActivity
public class MainPresenter implements IMainContract.IPresenter {

    public IMainContract.MainView mMainView;
    int mInitNaviState = 0;//0：未初始化；1：初始化中；2：已初始化
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
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            mInitNaviState = 2;
            return;
        }
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, mMainView.getActivity().getString(R.string.charge_map_TTS_APP_ID));
//        bundle.putString(BNCommonSettingParam.TTS_APP_ID,"9441461");
        BNaviSettingManager.setNaviSdkParam(bundle);
        BaiduNaviManager.getInstance().init(mMainView.getActivity(), Environment.getExternalStorageDirectory().getPath(), mMainView.getActivity().getPackageName(),
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
//                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        if (!"".equals(authinfo)) {
                            ThreadUtils.runOnMainThread(new Runnable() {

                                @Override
                                public void run() {
                                    mMainView.showSnackbar(authinfo);
                                }
                            });
                        }
                    }

                    @Override
                    public void initSuccess() {
//                        mMainView.showSnackbar("百度导航引擎初始化成功");
                        mInitNaviState = 2;
                    }

                    @Override
                    public void initStart() {
//                        mMainView.showSnackbar("百度导航引擎初始化开始");
                    }

                    @Override
                    public void initFailed() {
                        mMainView.showSnackbar("百度导航引擎初始化失败");
                        mInitNaviState = 0;
                    }
                }, null /*mTTSCallback*/, null, null);
    }

    @Override
    public void routePlanToNavi(LatLng destinationLatLng, String description) {
        BDLocation location = ChargeApplication.getInstance().getCurLocation();
        if (location == null){
            if (mInitNaviState == 0) {
                mMainView.showSnackbar("百度导航引擎还未初始化");
                MainActivityPermissionsDispatcher.needsPermissionWithCheck(mMainView.getActivity());
                return;
            }else if (mInitNaviState ==1){
                mMainView.showSnackbar("百度导航引擎正在初始化");
                return;
            }
            return;
        }
        //百度导航对android7.0系统支持不好，android7.0以上的直接打开手机百度地图app导航
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            // 构建 导航参数
            NaviParaOption para = new NaviParaOption()
                    .startPoint(new LatLng(location.getLatitude(), location.getLongitude())).endPoint(destinationLatLng)
                    .startName(location.getStreet() + location.getStreetNumber()).endName(description + "充电桩");

            try {
                // 调起百度地图导航
                boolean result = BaiduMapNavigation.openBaiduMapNavi(para, mMainView.getActivity());
                if (!result) {
                    mMainView.showSnackbar("无法打开百度地图，请检查是否安装了该app");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mMainView.showSnackbar("无法打开百度地图app");
            }
            return;
        }
        if (mInitNaviState == 0) {
            mMainView.showSnackbar("百度导航引擎还未初始化");
            MainActivityPermissionsDispatcher.needsPermissionWithCheck(mMainView.getActivity());
            return;
        }else if (mInitNaviState ==1){
            mMainView.showSnackbar("百度导航引擎正在初始化");
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
    public void onScanningResult(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
            String result = bundle.getString(CodeUtils.RESULT_STRING);
            mMainView.showSnackbar("解析结果:" + result);
            mMainView.showChargeDetailActivity(new MapMarkerInfo());
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

    class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        DemoRoutePlanListener(BNRoutePlanNode node) {
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
