package com.gopaychan.constantcharge.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.BaseFragment;
import com.gopaychan.constantcharge.home.HomeFragment;
import com.gopaychan.constantcharge.home.HomePresenter;
import com.gopaychan.constantcharge.home.HomePresenterModule;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements IMainContract.IView {
    @Inject
    public MainPresenter mPresenter;
    @Inject
    public HomePresenter mHomePresenter;

    @Bind(R.id.mainBottomNavigationBarId)
    BottomNavigationBar mBottomNavigationBar;
    @Bind(R.id.mainViewPagerId)
    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void initView() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img1, R.string.bottom_navigation_bar_txt1).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img2, R.string.bottom_navigation_bar_txt2).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img3, R.string.bottom_navigation_bar_txt3).setActiveColorResource(R.color.colorTheme))
                .addItem(new BottomNavigationItem(R.drawable.bottom_navigation_bar_img5, R.string.bottom_navigation_bar_txt5))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mPresenter.onTabSelected(position);
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
    }

    @Override
    protected void inject() {
        DaggerMainComponent.builder().mainPresenterModule(new MainPresenterModule(this))
                .homePresenterModule(new HomePresenterModule((HomeFragment) mFragments.get(0)))
                .build()
                .inject(this);

    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.getInstance());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
    }

    @Override
    public void showFragment(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {

    }
}
