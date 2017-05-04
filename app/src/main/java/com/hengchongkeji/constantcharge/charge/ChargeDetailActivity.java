package com.hengchongkeji.constantcharge.charge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.DaggerActivityComponent;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.domain.ChargeDetailData;
import com.hengchongkeji.constantcharge.data.domain.CurrentVoltage;
import com.hengchongkeji.constantcharge.data.domain.Temperature;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.view.RingProgress;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;


/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeDetailActivity extends ActionBarActivity {

    @Bind(R.id.chargeDetailSwipeRefreshLyId)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.chargeDetailViewStubId)
    ViewStub mViewStub;
    private boolean isInflater = false;

    RingProgress mRingProgress;
    LinearLayout mTimeLl;
    LinearLayout mMoneyLl;
    LinearLayout mCountLl;
    LinearLayout[] mLayouts;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    TextView mPercentTv;
    TextView mTimeTv;
    Button mStopChargeBtn;
    Map<LinearLayout, View[]> mLayoutViewMap;
    Fragment[] fragments = new Fragment[2];
    ChargeDetailData mChargeDetailData;
    @Inject
    ThreadExecutor mThreadExecutor;
    public static final String TO_CHARGE_DETAIL_ACTIVITY_ARGS = "charge_detail_activity_info";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_detail);
    }

    @Override
    protected void initView() {
        setTitle(R.string.home_charge_detail_title);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    protected void initData() {
    }

    private void loadData() {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mChargeDetailData = DataFactory.getInstance().getDataSource(true).getChargeDetailData();
                fragments = initViewData(mChargeDetailData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isInflater) {
                            isInflater = true;
                            inflater(mViewStub.inflate());
                        }
                        mRingProgress.setProgress(Integer.valueOf(mChargeDetailData.mPercent) * 360 / 100);
                        mPercentTv.setText(mChargeDetailData.mPercent + "%");
                        mTimeTv.setText("预计" + mChargeDetailData.mCompleteTime + "后充电完成");

                        for (int i = 0; i < mLayouts.length; i++) {
                            View[] vs = mLayoutViewMap.get(mLayouts[i]);
                            TextView tv1 = (TextView) vs[1];
                            switch (i) {
                                case 0:
                                    tv1.setText(mChargeDetailData.mChargeTime);
                                    break;
                                case 1:
                                    tv1.setText(mChargeDetailData.mChargeMoney);
                                    break;
                                case 2:
                                    tv1.setText(mChargeDetailData.mChargeCount);
                                    break;
                            }
                        }
                        mViewPager.getAdapter().notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private Fragment[] initViewData(ChargeDetailData chargeDetailData) {
        Fragment[] fragments = new Fragment[2];
        int cvSize = chargeDetailData.mCurrentVoltages.size();
        String[] currHours = new String[cvSize];
        String[] currStrings = new String[cvSize];
        for (int i = 0; i < cvSize; i++) {
            CurrentVoltage currentVoltage = mChargeDetailData.mCurrentVoltages.get(i);
            currHours[i] = currentVoltage.hours;
            currStrings[i] = currentVoltage.currentVoltage;
        }
        Bundle args0 = new Bundle();
        args0.putStringArray(ChargeDetailBaseFragment.MONTH_TEXT, currHours);
        args0.putStringArray(ChargeDetailBaseFragment.SCORE, currStrings);
        args0.putString(ChargeDetailBaseFragment.KEY, getString(R.string.charge_detail_max_voltage));
        args0.putString(ChargeDetailBaseFragment.VALUE, chargeDetailData.mMaxVoltages + "KW");
        args0.putString(ChargeDetailBaseFragment.MIN_SCORE, chargeDetailData.mMinVoltages);
        args0.putString(ChargeDetailBaseFragment.MAX_SCORE, chargeDetailData.mMaxVoltages);
        ChargeDetailBaseFragment fragment0 = ChargeDetailBaseFragment.getInstance(args0);
        fragments[0] = fragment0;


        int tSize = chargeDetailData.mTemperatures.size();
        String[] tHours = new String[tSize];
        String[] tStrings = new String[tSize];
        for (int i = 0; i < cvSize; i++) {
            Temperature temperature = mChargeDetailData.mTemperatures.get(i);
            tHours[i] = temperature.hour;
            tStrings[i] = temperature.temperature;
        }
        Bundle args1 = new Bundle();
        args1.putStringArray(ChargeDetailBaseFragment.MONTH_TEXT, tHours);
        args1.putStringArray(ChargeDetailBaseFragment.SCORE, tStrings);
        args1.putString(ChargeDetailBaseFragment.KEY, getString(R.string.charge_detail_max_temperature));
        args1.putString(ChargeDetailBaseFragment.VALUE, chargeDetailData.mMaxTemperature + "°");
        args1.putString(ChargeDetailBaseFragment.MIN_SCORE, chargeDetailData.mMinTemperature);
        args1.putString(ChargeDetailBaseFragment.MAX_SCORE, chargeDetailData.mMaxTemperature);
        ChargeDetailBaseFragment fragment1 = ChargeDetailBaseFragment.getInstance(args1);
        fragments[1] = fragment1;
        return fragments;
    }

    private void inflater(View root) {
        mRingProgress = (RingProgress) root.findViewById(R.id.electricityRingProgressId);
        mTimeLl = (LinearLayout) root.findViewById(R.id.chargeDetailTimeId);
        mMoneyLl = (LinearLayout) root.findViewById(R.id.chargeDetailMoneyId);
        mCountLl = (LinearLayout) root.findViewById(R.id.chargeDetailCountId);
        mTabLayout = (TabLayout) root.findViewById(R.id.chargeDetailTabLyId);
        mViewPager = (ViewPager) root.findViewById(R.id.chargeDetailVpId);
        mPercentTv = (TextView) root.findViewById(R.id.chargeDetailPercentTvId);
        mTimeTv = (TextView) root.findViewById(R.id.chargeDetailTimeTvId);
        mStopChargeBtn = (Button) root.findViewById(R.id.chargeDetailEndId);
        mStopChargeBtn.setOnClickListener(new View.OnClickListener() {//结束充电
            @Override
            public void onClick(View v) {

            }
        });
        mLayouts = new LinearLayout[]{mTimeLl, mMoneyLl, mCountLl};
        mLayoutViewMap = new HashMap<>();
        for (int i = 0; i < mLayouts.length; i++) {
            View[] views = new View[3];
            views[0] = mLayouts[i].findViewById(R.id.chargeItemIvId);
            views[1] = mLayouts[i].findViewById(R.id.chargeItemTv1Id);
            views[2] = mLayouts[i].findViewById(R.id.chargeItemTv2Id);
            mLayoutViewMap.put(mLayouts[i], views);
        }
        for (int i = 0; i < mLayouts.length; i++) {
            View[] vs = mLayoutViewMap.get(mLayouts[i]);
            ImageView iv = (ImageView) vs[0];
            TextView tv2 = (TextView) vs[2];
            switch (i) {
                case 0:
                    tv2.setText(getString(R.string.charge_detail_time));
                    iv.setBackgroundResource(R.drawable.charge_detail_time);
                    break;
                case 1:
                    tv2.setText(getString(R.string.charge_detail_money));
                    iv.setBackgroundResource(R.drawable.charge_detail_money);
                    break;
                case 2:
                    tv2.setText(getString(R.string.charge_detail_count));
                    iv.setBackgroundResource(R.drawable.charge_detail_count);
                    break;
            }
        }

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return getString(R.string.charge_detail_current_voltage);
                else return getString(R.string.charge_detail_battery_temperature);
            }

            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.setTabTextColors(getResources().getColor(R.color.chargeDetailTextColor), getResources().getColor(R.color.chargeDetailTextColor));
    }

    @Override
    protected void inject() {
        DaggerActivityComponent.builder().applicationComponent(getApplicationComponent()).activityModule(getActivityModule()).build().inject(this);
    }

}
