package com.gopaychan.constantcharge.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.BaseFragment;
import com.gopaychan.constantcharge.home.map.ChargeListFragment;
import com.gopaychan.constantcharge.home.map.ChargeMapFragment;
import com.gopaychan.constantcharge.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class HomeFragmentV2 extends BaseFragment implements IHomeContract.IView {

    @Bind(R.id.homeCardViewId)
    CardView mCardView;
    @Bind(R.id.homeLocationPbId)
    ProgressBar mProgressBar;
    @Bind(R.id.homeLocationTvId)
    TextView mCityTv;
    @Bind(R.id.homeSwitchModeTvId)
    TextView mModeTv;
    @Bind(R.id.mainPbId)
    ProgressBar mHorizontalProgressBar;
    private IHomeContract.IPresenter mPresenter;
    private ChargeMapFragment mMapFragment;
    private ChargeListFragment mListFragment;

    public static HomeFragmentV2 getInstance() {
        Bundle args = new Bundle();

        HomeFragmentV2 fragment = new HomeFragmentV2();
        fragment.setArguments(args);
        fragment.mMapFragment = ChargeMapFragment.getInstance();
        fragment.mListFragment = ChargeListFragment.getInstance();
        return fragment;
    }

    public ChargeMapFragment getMapFragment() {
        return mMapFragment;
    }

    public ChargeListFragment getListFragment() {
        return mListFragment;
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
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_v2, container, false);
        ButterKnife.bind(this, view);
        initFragment();
        mPresenter.start();
        return view;
    }

    private void initFragment() {
        getChildFragmentManager().beginTransaction().add(R.id.homeFragmentLyId, mMapFragment).add(R.id.homeFragmentLyId, mListFragment).hide(mListFragment).show(mMapFragment).commit();

    }

    @Override
    public void setPresenter(IHomeContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.homeCardViewId)
    public void showSearchView() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }


    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mCityTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar(final String city) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mCityTv.getText().toString().equals(city)) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mCityTv.setVisibility(View.VISIBLE);
                    mCityTv.setText(city);
                }
            }
        });
    }

    @Override
    public void showMapFragment() {
        getChildFragmentManager().beginTransaction().show(mMapFragment).hide(mListFragment).commit();
    }

    @Override
    public void showListFragment() {
        getChildFragmentManager().beginTransaction().hide(mMapFragment).show(mListFragment).commit();
    }

    @Override
    public void showModeText(String mode) {
        mModeTv.setText(mode);
    }

    @Override
    public void showHorizontalProgressBar() {
        mHorizontalProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHorizontalProgressBar() {
        mHorizontalProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.homeSwitchModeTvId)
    public void switchMode(){
        mPresenter.resetMode();
    }
}
