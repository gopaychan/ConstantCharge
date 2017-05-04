package com.hengchongkeji.constantcharge.main.home.map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.data.domain.MapMarkerInfo;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gopayChan on 2017/4/20.
 */

public class ChargeMapPresenter implements IChargeMapContract.IPresenter {

    private OrientationListener mOrientationListener;
    private IChargeMapContract.IView mView;
    private BaiduMap mBaiduMap;
    private boolean hasLocationed;
    private List<MapMarkerInfo> mMapMarkerInfoList;
    //当前定位的模式
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private ThreadExecutor mThreadExecutor;
    private Context mContext;
    private Marker mPreMarker;

    @Inject
    public ChargeMapPresenter(IChargeMapContract.IView view, Context context, ThreadExecutor threadExecutor) {
        mView = view;
        mContext = context;
        mThreadExecutor = threadExecutor;
        mOrientationListener = new OrientationListener(context);
    }

    @Inject
    public void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        initBaiduMap();
        setChargeBalance();
        initMarkerClick();
//        initNavi();
        onStart();
    }

    private void initMarkers(final BDLocation location) {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mMapMarkerInfoList = DataFactory.getInstance().getDataSource(true).getLatLngNearby(new LatLng(location.getLatitude(), location.getLongitude()));
                for (MapMarkerInfo makerInfo : mMapMarkerInfoList) {
                    mView.showMarker(makerInfo);
                }
            }
        });
    }

    private void initMarkerClick() {
        //对Marker的点击
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //获得marker中的数据
                mPreMarker = marker;
                mView.showPopupWindow(marker);
                return true;
            }
        });
    }

    private void setChargeBalance() {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String balance = DataFactory.getInstance().getDataSource(true).getChargeBalance();
                mView.showChargeBalanceText(balance);
            }
        });
    }


    @Override
    public void onStart() {
        // 开启定位图层
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(true);
            mOrientationListener.start();
        }
    }

    @Override
    public void onStop() {
        if (mBaiduMap != null) {
            // 当不需要定位图层时关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mOrientationListener.stop();
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onMapPause() {
        mView.hideMapView();
    }

    @Override
    public void onMapResume() {
        mView.showMapView();
    }

    @Override
    public void onLocationChange(BDLocation location) {
        if (mBaiduMap != null) {
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//          mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, null);
            mBaiduMap.setMyLocationConfigeration(config);
            if (!hasLocationed) {
                hasLocationed = true;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                initMarkers(location);
            }
        }
    }

    @Override
    public void location() {
        hasLocationed = false;
    }

    private void initBaiduMap() {
        // 设置定位的相关配置
        mBaiduMap = mView.getBaiduMap();
        //默认地图放大倍数
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        //开启交通图
//        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mView.hidePopupWindow(mPreMarker);
                mPreMarker = null;
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mView.hidePopupWindow(mPreMarker);
                mPreMarker = null;
                return false;
            }
        });
    }

    private class OrientationListener implements SensorEventListener {

        private Context context;
        private SensorManager sensorManager;
        private Sensor sensor;

        private float lastX;

        public OrientationListener(Context context) {
            this.context = context;
        }

        // 开始
        public void start() {
            // 获得传感器管理器
            sensorManager = (SensorManager) context
                    .getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                // 获得方向传感器
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            // 注册
            if (sensor != null) {//SensorManager.SENSOR_DELAY_UI
                sensorManager.registerListener(OrientationListener.this, sensor,
                        SensorManager.SENSOR_DELAY_UI);
            }

        }

        // 停止检测
        public void stop() {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 接受方向感应器的类型
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                // 这里我们可以得到数据，然后根据需要来处理
                float x = event.values[SensorManager.DATA_X];
                if (Math.abs(x - lastX) > 1.0) {
                    BDLocation location = ChargeApplication.getInstance().getCurLocation();
                    if (location != null) {
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(location.getRadius())
                                .direction(x)
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude()).build();
                        mBaiduMap.setMyLocationData(locData);
                        MyLocationConfiguration config = new MyLocationConfiguration(
                                mCurrentMode, true, null);
                        mBaiduMap.setMyLocationConfigeration(config);
                    }
                }
                lastX = x;
            }
        }
    }
}
