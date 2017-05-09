package com.hengchongkeji.constantcharge.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.main.home.map.ChargeListFragment;
import com.hengchongkeji.constantcharge.main.home.map.ChargeMapFragment;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static com.hengchongkeji.constantcharge.R.id.mainPbId;

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
    @Bind(mainPbId)
    ProgressBar mHorizontalProgressBar;
    @Bind(R.id.homeSpaceViewId)
    View view;
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
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_v2;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        initFragment();
        if (mPresenter == null) return;
        mPresenter.start();
        if (ScreenUtils.canChangeStatusColor())
            setSpaceViewHeight();
    }

    private void setSpaceViewHeight() {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ScreenUtils.getStatusHeight(getActivity());
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mHorizontalProgressBar.getLayoutParams();
        params1.setMargins(0, ScreenUtils.getStatusHeight(getActivity()) + ScreenUtils.dip2px(getActivity(), 50), 0, 0);
    }

    private void initFragment() {
        if (mMapFragment != null && mListFragment != null)
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
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mCityTv.setVisibility(View.VISIBLE);
                }
                if (!mCityTv.getText().toString().equals(city)) {
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

    @Override
    public void showSnackbar(String msg) {
        Snackbar.make(mCardView, msg, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.homeSwitchModeTvId)
    public void switchMode() {
        mPresenter.resetMode();
    }
}
