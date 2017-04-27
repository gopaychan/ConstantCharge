//package com.gopaychan.constantcharge.home;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.CardView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.gopaychan.constantcharge.R;
//import com.gopaychan.constantcharge.base.BaseFragment;
//import com.gopaychan.constantcharge.charge.ChargeCollectionActivity;
//import com.gopaychan.constantcharge.charge.ChargeDetailActivity;
//import com.gopaychan.constantcharge.home.map.ChargeMapActivity;
//import com.gopaychan.constantcharge.charge.ChargeOnLineActivity;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by gopaychan on 2017/3/26.
// */
//
//public class HomeFragmentV1 extends BaseFragment implements IHomeContract.IView{
//
//    @Bind(R.id.homeCardViewId)
//    CardView mCardView;
//    @Bind(R.id.homeLocationPbId)
//    ProgressBar mProgressBar;
//    @Bind(R.id.homeLocationTvId)
//    TextView mCityTv;
//    private IHomeContract.IPresenter mPresenter;
//
//    public static HomeFragmentV1 getInstance() {
//        Bundle args = new Bundle();
//
//        HomeFragmentV1 fragment = new HomeFragmentV1();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPresenter = new HomePresenter(this,getActivity().getApplicationContext());
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home_v1,container,false);
//        ButterKnife.bind(this,view);
//        mPresenter.start();
//        return view;
//    }
//
//    @Override
//    public void setPresenter(IHomeContract.IPresenter presenter) {
//        mPresenter = presenter;
//    }
//
//    @OnClick(R.id.homeCardViewId)
//    public void showSearchView() {
//        Intent intent = new Intent(getActivity(),SearchActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.showChargeDetailTv)
//    public void showChargeDetail() {
//        Intent intent = new Intent(getActivity(),ChargeDetailActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.showChargePlaceCollectionTv)
//    public void showChargePlaceCollection() {
//        Intent intent = new Intent(getActivity(),ChargeCollectionActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.showChargeMapTv)
//    public void showChargeMap() {
//        Intent intent = new Intent(getActivity(),ChargeMapActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.showOnlineRechargeTv)
//    public void showOnlineRecharge() {
//        Intent intent = new Intent(getActivity(),ChargeOnLineActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.showReserveChargeTv)
//    public void showReserveCharge(){
//    }
//
//    @Override
//    public void showProgressBar() {
//        mProgressBar.setVisibility(View.VISIBLE);
//        mCityTv.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void hideProgressBar(final String city) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mProgressBar.setVisibility(View.INVISIBLE);
//                mCityTv.setVisibility(View.VISIBLE);
//                mCityTv.setText(city);
//            }
//        });
//    }
//
//    @Override
//    public void showMapFragment() {
//
//    }
//
//    @Override
//    public void showListFragment() {
//
//    }
//}
