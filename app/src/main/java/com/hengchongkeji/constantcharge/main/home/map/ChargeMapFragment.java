package com.hengchongkeji.constantcharge.main.home.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.charge.ChargeDetailActivity;
import com.hengchongkeji.constantcharge.data.entity.MapMarkerInfo;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static com.baidu.mapapi.map.BitmapDescriptorFactory.fromResource;
import static com.hengchongkeji.constantcharge.charge.ChargeDetailActivity.TO_CHARGE_DETAIL_ACTIVITY_ARGS;


/**
 * Created by gopayChan on 2017/4/25.
 */

public class ChargeMapFragment extends BaseFragment implements IChargeMapContract.IView {
    @Bind(R.id.chargeMapViewId)
    MapView mMapView;

    View mBubbleLayout;
    TextView mAddressTv, mFreePileTv, mTotalPileTv, mPredictTvId;
    Button mDetailBtn, mNavigationBtn;

    IChargeMapContract.IPresenter mPresenter;

    private Marker mPreMarker;
    private static final String INFO = "info";
    private BitmapDescriptor mNormalMarkerIcon;
    private BitmapDescriptor mFocusMarkerIcon;
    private BitmapDescriptor mNotFreePileIcon;

    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public static ChargeMapFragment getInstance() {
        return new ChargeMapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            activity.registerOnLocationChangeListener(new MainActivity.OnLocationChangeListener() {
                @Override
                public void onChange(BDLocation location) {
                    mPresenter.onLocationChange(location);
                }

                @Override
                public void onFail(String failStr) {
                    mPresenter.onLocationFail(failStr);
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNormalMarkerIcon =
                fromResource(R.drawable.charge_map_maker);
        mFocusMarkerIcon =
                fromResource(R.drawable.charge_map_maker_focus);
        mNotFreePileIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.charge_map_maker_no_free_pile);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_charge_map;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        if (mPresenter == null) return;
        mPresenter.start();
        initPopupWindow();
    }

    private void initPopupWindow() {
        mBubbleLayout = LayoutInflater.from(getActivity()).inflate(R.layout.popup_charge_map, null);
        mAddressTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapAddressTvId);
        mTotalPileTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapTotalTvId);
        mFreePileTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapFreeTvId);
        mPredictTvId = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapPredictTvId);
        mDetailBtn = (Button) mBubbleLayout.findViewById(R.id.popupChargeMapDetailBtnId);
        mNavigationBtn = (Button) mBubbleLayout.findViewById(R.id.popupChargeMapnavigationBtnId);
    }

    @OnClick(R.id.chargeMapLocationIvId)
    public void location(){
        mPresenter.location();
    }

    @Override
    public void setPresenter(IChargeMapContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSnackbar(final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mMapView, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showChargeBalanceText(final String balance) {
    }

    @Override
    public BaiduMap getBaiduMap() {
        return mMapView.getMap();
    }

    @Override
    public void showPopupWindow(final Marker marker) {
        final MapMarkerInfo mapMarkerInfo = (MapMarkerInfo) marker.getExtraInfo().get(INFO);
        if (mPreMarker != null)
            mPreMarker.setIcon(mNormalMarkerIcon);
        marker.setIcon(mFocusMarkerIcon);
        mPreMarker = marker;
        mAddressTv.setText(mapMarkerInfo.address);
        mTotalPileTv.setText(getString(R.string.charge_map_popup_total_pile).replace("{}", mapMarkerInfo.totalPile));
        mFreePileTv.setText(getString(R.string.charge_map_popup_free_pile).replace("{}", mapMarkerInfo.freePile));
        if ("0".equals(mapMarkerInfo.predictFreePileTime))
            mPredictTvId.setText(getString(R.string.charge_map_popup_predict_reach_time).replace("{}", mapMarkerInfo.predictReachTime));
        else
            mPredictTvId.setText(getString(R.string.charge_map_popup_predict_free_pile).replace("{}", mapMarkerInfo.predictFreePileTime));
        InfoWindow mInfoWindow;
        //为弹出的InfoWindow添加点击事件
        mInfoWindow = new InfoWindow(mBubbleLayout, mapMarkerInfo.latLng, -47);
        //显示InfoWindow
        getBaiduMap().showInfoWindow(mInfoWindow);
        mDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChargeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TO_CHARGE_DETAIL_ACTIVITY_ARGS, mapMarkerInfo);
                startActivity(intent);
                hidePopupWindow(marker);
            }
        });
        mNavigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    ((MainActivity) activity).mMainPresenter.routePlanToNavi(mapMarkerInfo.latLng, mapMarkerInfo.address);
                }
                hidePopupWindow(marker);
            }
        });
    }

    @Override
    public void hidePopupWindow(Marker marker) {
        if (mPreMarker != null) {
            getBaiduMap().hideInfoWindow();
            final MapMarkerInfo mapMarkerInfo = (MapMarkerInfo) marker.getExtraInfo().get(INFO);
            if ("0".equals(mapMarkerInfo.freePile)) {
                mPreMarker.setIcon(mNotFreePileIcon);
            } else {
                mPreMarker.setIcon(mNormalMarkerIcon);
            }
            mPreMarker = null;
        }
    }

    @Override
    public void showMarker(MapMarkerInfo mapMarkerInfo) {
        LatLng point = mapMarkerInfo.latLng;
        //构建Marker图标
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions option = new MarkerOptions()
                .position(point);
        if ("0".equals(mapMarkerInfo.freePile)) {
            option.icon(mNotFreePileIcon);
        } else {
            option.icon(mNormalMarkerIcon);
        }
        //在地图上添加Marker，并显示
        Marker marker = (Marker) (getBaiduMap().addOverlay(option));
        Bundle bundle = new Bundle();
        bundle.putSerializable(INFO, mapMarkerInfo);
        marker.setExtraInfo(bundle);
    }

    @Override
    public void showMapView() {
        if (mMapView != null) {
            mMapView.setVisibility(View.VISIBLE);
            mMapView.onResume();
        }
    }

    @Override
    public void hideMapView() {
        if (mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
            mMapView.onPause();
        }
    }
}
