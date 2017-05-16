package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType.GCJ02;
import static com.hengchongkeji.constantcharge.main.MainActivity.activityList;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_FAIL;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_INIT_NOW;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_NO_INIT;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_OPEN_BAIDU_FAIL;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_START;
import static com.hengchongkeji.constantcharge.main.home.map.BaiduNaviManager.onRoutePlanResponse.PLAN_SUCCESS;
import static com.hengchongkeji.constantcharge.main.home.map.ChargeMapFragment.ROUTE_PLAN_NODE;


/**
 * Created by gopayChan on 2017/5/5.
 */

public class BaiduNaviManager {
    public static final int INIT_NAVI_FAIL = 0;
    public static final int INIT_NAVI_RUNNING = 1;
    public static final int INIT_NAVI_SUCCESS = 2;
    public static final int INIT_NAVI_KEY_CHECK_FAIL = 3;
    public static int mInitNaviState = INIT_NAVI_FAIL;
    public static boolean hasPermission = false;
    private static BaiduNaviManager mInstance = new BaiduNaviManager();

    private BaiduNaviManager() {

    }

    public static BaiduNaviManager getInstance() {
        return mInstance;
    }

    public void initNavi(Activity context, final onInitResponse response) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            mInitNaviState = INIT_NAVI_SUCCESS;
            return;
        }
        if (mInitNaviState == INIT_NAVI_SUCCESS || mInitNaviState == INIT_NAVI_RUNNING){
            return;
        }
        mInitNaviState = INIT_NAVI_RUNNING;
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, context.getApplicationContext().getString(R.string.charge_map_TTS_APP_ID));
        BNaviSettingManager.setNaviSdkParam(bundle);
        com.baidu.navisdk.adapter.BaiduNaviManager.getInstance().init(context, Environment.getExternalStorageDirectory().getPath(), context.getApplicationContext().getPackageName(),
                new com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String authinfo = "";
                        if (0 == status) {
//                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        if (!"".equals(authinfo)) {
                            ThreadUtils.runOnMainThread(new Runnable() {

                                @Override
                                public void run() {
                                    response.onResponse(INIT_NAVI_KEY_CHECK_FAIL);
                                }
                            });
                        }
                    }

                    @Override
                    public void initSuccess() {
                        mInitNaviState = INIT_NAVI_SUCCESS;
                    }

                    @Override
                    public void initStart() {
                    }

                    @Override
                    public void initFailed() {
                        mInitNaviState = INIT_NAVI_FAIL;
                        response.onResponse(INIT_NAVI_FAIL);
                    }
                }, null /*mTTSCallback*/, null, null);
    }

    public void routePlanToNavi(Activity activity, BDLocation startLocation, LatLng destinationLatLng, String description, onRoutePlanResponse response) {
        if (startLocation == null) {
            if (mInitNaviState == INIT_NAVI_FAIL) {
                response.onResponse(PLAN_NO_INIT);
                return;
            } else if (mInitNaviState == INIT_NAVI_RUNNING) {
                response.onResponse(PLAN_INIT_NOW);
                return;
            }
            return;
        }
        //百度导航对android7.0系统支持不好，android7.0以上的直接打开手机百度地图app导航
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            // 构建 导航参数
            NaviParaOption para = new NaviParaOption()
                    .startPoint(new LatLng(startLocation.getLatitude(), startLocation.getLongitude())).endPoint(destinationLatLng)
                    .startName(startLocation.getStreet() + startLocation.getStreetNumber()).endName(description + "充电桩");

            try {
                // 调起百度地图导航
                boolean result = BaiduMapNavigation.openBaiduMapNavi(para, activity);
                if (!result) {
                    response.onResponse(PLAN_OPEN_BAIDU_FAIL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.onResponse(PLAN_OPEN_BAIDU_FAIL);
            }
            return;
        }
        if (mInitNaviState == INIT_NAVI_FAIL) {
            response.onResponse(PLAN_NO_INIT);
//            MainActivityPermissionsDispatcher.needsPermissionWithCheck(mMainView.getActivity());
            return;
        } else if (mInitNaviState == INIT_NAVI_RUNNING) {
            response.onResponse(PLAN_INIT_NOW);
            return;
        }
        response.onResponse(PLAN_START);
        BNRoutePlanNode sNode = new BNRoutePlanNode(startLocation.getLongitude(), startLocation.getLatitude(), startLocation.getStreet() + startLocation.getStreetNumber(), null, GCJ02);
        BNRoutePlanNode eNode = new BNRoutePlanNode(destinationLatLng.longitude, destinationLatLng.latitude, description + "充电桩", null, GCJ02);
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        com.baidu.navisdk.adapter.BaiduNaviManager.getInstance().launchNavigator(activity, list, 1, true, new DemoRoutePlanListener(sNode, activity, response));
    }

    private class DemoRoutePlanListener implements com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        private Activity mActivity;
        private onRoutePlanResponse mResponse;

        DemoRoutePlanListener(BNRoutePlanNode node, Activity activity, onRoutePlanResponse response) {
            mBNRoutePlanNode = node;
            mActivity = activity;
            mResponse = response;
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
            Intent intent = new Intent(mActivity, ChargeNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            mResponse.onResponse(PLAN_SUCCESS);
            mActivity.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            mResponse.onResponse(PLAN_FAIL);
        }
    }

    public interface onInitResponse {
        void onResponse(int result);
    }

    public interface onRoutePlanResponse {

        int PLAN_NO_INIT = 10;
        int PLAN_INIT_NOW = 11;

        int PLAN_OPEN_BAIDU_FAIL = 12;

        int PLAN_START = 13;
        int PLAN_SUCCESS = 14;
        int PLAN_FAIL = 15;

        void onResponse(int result);
    }


}
