package com.hengchongkeji.constantcharge.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.BDLocation;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.hengchongkeji.constantcharge.main.found.FoundFragment;
import com.hengchongkeji.constantcharge.main.home.HomeFragmentModule;
import com.hengchongkeji.constantcharge.main.home.HomeFragmentV2;
import com.hengchongkeji.constantcharge.main.home.HomePresenter;
import com.hengchongkeji.constantcharge.main.home.map.ChargeMapPresenter;
import com.hengchongkeji.constantcharge.main.mine.MineFragment;
import com.hengchongkeji.constantcharge.main.scanning.ScanningFragment;
import com.hengchongkeji.constantcharge.utils.PermissionUtils;
import com.hengchongkeji.constantcharge.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
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
    @Bind(R.id.mainRootRyt)
    RelativeLayout mRootRyt;
    private ArrayList<Fragment> mFragments;
    public static List<Activity> activityList = new ArrayList<>();
    public final static int SCANNING_REQUEST_CODE = 0x12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityList.add(this);
        if (ScreenUtils.canChangeStatusColor())
            mRootRyt.setPadding(0, 0, 0, ScreenUtils.getBottomStatusHeight(this));
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivityPermissionsDispatcher.needsPermissionWithCheck(MainActivity.this);
            }
        }, 1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
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
                if (position == 3) {
                    mFragments.get(3).setUserVisibleHint(true);
                }
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
        mFragments.add(ScanningFragment.getInstance());
        mFragments.add(FoundFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapPresenter.onMapResume();
        if (mFragments != null) {
            Fragment fragment = mFragments.get(3);
            if (fragment != null)
                fragment.setUserVisibleHint(true);
        }
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

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE})
    void needsPermission() {
        mMainPresenter.start();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {
        PermissionUtils.showRationaleDialog(this, "导航、显示附近充电桩需要定位权限和读取文件权限，是否弹出权限申请", request);
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE})
    void permissionDenied() {
        mMainPresenter.mMainView.showSnackbar("拒绝定位权限将无法准确找到附近的充电桩,拒绝读取文件权限将无法正常导航");
        mHomePresenter.mView.hideProgressBar("未知");
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE})
    void permissionNerverAskAgain() {
        mMainPresenter.mMainView.showSnackbar("定位权限，读取文件权限请求将不再弹出，如需正常使用功能请到手机设置中打开权限");
        mHomePresenter.mView.hideProgressBar("未知");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public interface OnLocationChangeListener {
        void onChange(BDLocation location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == SCANNING_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                mMainPresenter.onScanningResult(data);
            }
        }
    }
}
