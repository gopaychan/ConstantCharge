package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.hengchongkeji.constantcharge.base.IBasePresenter;
import com.hengchongkeji.constantcharge.base.IBaseView;
import com.hengchongkeji.constantcharge.data.domain.MapMarkerInfo;

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

        void showMarker(MapMarkerInfo mapMarkerInfo);

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

        void location();

    }

//    class ChargeMapView implements IView {
//        private ChargeMapActivity mActivity;
//        private Marker mPreMarker;
//        private BitmapDescriptor mNormalMarkerIcon;
//        private BitmapDescriptor mFocusMarkerIcon;
//        private BitmapDescriptor mNotFreePileIcon;
//        private static final String INFO = "info";
//        public static final String TO_CHARGEDETAILACTIVITY_ARGS = "charge_detail_activity_info";
//        public static final String TO_BAIDUACTIVITY_ARGS = "baidu_guide_activity_info";
//
//        @Inject
//        public ChargeMapView(BaseActivity activity) {
//            mActivity = (ChargeMapActivity) activity;
//            mNormalMarkerIcon = BitmapDescriptorFactory
//                    .fromResource(R.drawable.charge_map_maker);
//            mFocusMarkerIcon = BitmapDescriptorFactory
//                    .fromResource(R.drawable.charge_map_maker_focus);
//            mNotFreePileIcon = BitmapDescriptorFactory
//                    .fromResource(R.drawable.charge_map_maker_no_free_pile);
//
//        }
//
//        IPresenter mPresenter;
//
//        @Override
//        public Activity getActivity() {
//            return mActivity;
//        }
//
//        @Override
//        public void setPresenter(IPresenter presenter) {
//            mPresenter = presenter;
//        }
//
//        @Override
//        public void showSnackbar(final String msg) {
//            ThreadUtils.runOnMainThread(new Runnable() {
//                @Override
//                public void run() {
//                    Snackbar.make(mActivity.mDistanceLl, msg, Snackbar.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        @Override
//        public void showChargeBalanceText(final String balance) {
//            ThreadUtils.runOnMainThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((TextView) mActivity.mLayoutViewMap.get(mActivity.mLayouts[1])[1]).setText(balance);
//                }
//            });
//        }
//
//        @Override
//        public BaiduMap getBaiduMap() {
//            return mActivity.mMapView.getMap();
//        }
//
//        @Override
//        public void showPopupWindow(Marker marker) {
//            final MapMarkerInfo mapMarkerInfo = (MapMarkerInfo) marker.getExtraInfo().get(INFO);
//            if (mPreMarker != null)
//                mPreMarker.setIcon(mNormalMarkerIcon);
//            marker.setIcon(mFocusMarkerIcon);
//            mPreMarker = marker;
//            mActivity.mAddressTv.setText(mapMarkerInfo.address);
//            mActivity.mTotalPileTv.setText(mActivity.getString(R.string.charge_map_popup_total_pile).replace("{}", mapMarkerInfo.totalPile));
//            mActivity.mFreePileTv.setText(mActivity.getString(R.string.charge_map_popup_free_pile).replace("{}", mapMarkerInfo.freePile));
//            if ("0".equals(mapMarkerInfo.predictFreePileTime))
//                mActivity.mPredictTvId.setText(mActivity.getString(R.string.charge_map_popup_predict_reach_time).replace("{}", mapMarkerInfo.predictReachTime));
//            else
//                mActivity.mPredictTvId.setText(mActivity.getString(R.string.charge_map_popup_predict_free_pile).replace("{}", mapMarkerInfo.predictFreePileTime));
//            InfoWindow mInfoWindow;
//            //为弹出的InfoWindow添加点击事件
//            mInfoWindow = new InfoWindow(mActivity.mBubbleLayout, mapMarkerInfo.latLng, -47);
//            //显示InfoWindow
//            getBaiduMap().showInfoWindow(mInfoWindow);
//            mActivity.mDetailBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, ChargeDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(TO_CHARGEDETAILACTIVITY_ARGS, mapMarkerInfo);
//                    mActivity.startActivity(intent);
//                }
//            });
//            mActivity.mNavigationBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPresenter.routePlanToNavi(mapMarkerInfo.latLng, mapMarkerInfo.address);
//                }
//            });
//        }
//
//        @Override
//        public void hidePopupWindow(Marker marker) {
//            if (mPreMarker != null) {
//                getBaiduMap().hideInfoWindow();
//                final MapMarkerInfo mapMarkerInfo = (MapMarkerInfo) marker.getExtraInfo().get(INFO);
//                if ("0".equals(mapMarkerInfo.freePile)){
//                    mPreMarker.setIcon(mNotFreePileIcon);
//                }else {
//                    mPreMarker.setIcon(mNormalMarkerIcon);
//                }
//                mPreMarker = null;
//            }
//        }
//
//        @Override
//        public void showMarker(MapMarkerInfo mapMarkerInfo) {
//            LatLng point = mapMarkerInfo.latLng;
//            //构建Marker图标
//            //构建MarkerOption，用于在地图上添加Marker
//            MarkerOptions option = new MarkerOptions()
//                    .position(point);
//            if ("0".equals(mapMarkerInfo.freePile)) {
//                option.icon(mNotFreePileIcon);
//            } else {
//                option.icon(mNormalMarkerIcon);
//            }
//            //在地图上添加Marker，并显示
//            Marker marker = (Marker) (getBaiduMap().addOverlay(option));
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(INFO, mapMarkerInfo);
//            marker.setExtraInfo(bundle);
//        }
//    }
}
