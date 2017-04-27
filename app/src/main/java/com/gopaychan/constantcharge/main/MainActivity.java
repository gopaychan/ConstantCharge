package com.gopaychan.constantcharge.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.BDLocation;
import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.BaseFragment;
import com.gopaychan.constantcharge.home.HomeFragmentModule;
import com.gopaychan.constantcharge.home.HomeFragmentV2;
import com.gopaychan.constantcharge.home.HomePresenter;
import com.gopaychan.constantcharge.home.map.ChargeMapPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;


public class MainActivity extends BaseActivity {
    @Inject
    public MainPresenter mMainPresenter;
    @Inject
    public HomePresenter mHomePresenter;
    @Inject
    ChargeMapPresenter mMapPresenter;

    @Bind(R.id.mainBottomNavigationBarId)
    BottomNavigationBar mBottomNavigationBar;
    @Bind(R.id.mainViewPagerId)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    public static List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityList.add(this);
        mMainPresenter.start();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.LOCATION}, 1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void initView() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img1, R.string.bottom_navigation_bar_txt1).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img3, R.string.bottom_navigation_bar_txt3).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img2, R.string.bottom_navigation_bar_txt2).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img5, R.string.bottom_navigation_bar_txt5).setActiveColorResource(R.color.colorTheme))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mMainPresenter.onTabSelected(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected void inject() {
        HomeFragmentV2 fragment = (HomeFragmentV2) mFragments.get(0);
        DaggerMainComponent.builder().applicationComponent(getApplicationComponent()).activityModule(getActivityModule())
                .homeFragmentModule(new HomeFragmentModule(fragment, fragment.getMapFragment()))
                .build()
                .inject(this);

    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragmentV2.getInstance());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapPresenter.onMapResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.onStart();
        mMapPresenter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapPresenter.onMapPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMainPresenter.onStop();
        mMapPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapPresenter.onDestroy();
    }


    public void registerOnLocationChangeListener(OnLocationChangeListener listener) {
        if (mMainPresenter != null)
            mMainPresenter.registerOnLocationChangeListener(listener);
    }

    public interface OnLocationChangeListener {
        void onChange(BDLocation location);
    }
}
