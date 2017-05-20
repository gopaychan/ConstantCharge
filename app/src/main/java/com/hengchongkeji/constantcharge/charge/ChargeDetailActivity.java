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
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.DaggerActivityComponent;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.Equipment;
import com.hengchongkeji.constantcharge.data.source.DataFactory;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.view.RingProgress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    Button mControlBtn;
    Map<LinearLayout, View[]> mLayoutViewMap;
    Fragment[] fragments = new Fragment[2];
    @Inject
    ThreadExecutor mThreadExecutor;
    public static final String EQUIPMENT_ID = "equipment_id";

    private List<String> mVoltageList;
    private List<String> mCurrentList;
    private long mLastTimeLong;
    private List<String> mTimeTextList;
    SimpleDateFormat mSdf;
    private int mMaxCurrent;
    private int mMinCurrent;
    private int mMaxVoltage;
    private int mMinVoltage;

    private boolean isStartCharge = false;
    private String mEquipmentId;

    private TimerTask mTimerTask;
    private Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEquipmentId = getIntent().getStringExtra(EQUIPMENT_ID);
        setContentView(R.layout.activity_charge_detail);
    }

    @Override
    protected void initView() {
        setTitle(R.string.home_charge_detail_title);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
        mRefreshLayout.setRefreshing(true);
        loadData(false);
    }

    @Override
    protected void initData() {
        mCurrentList = new ArrayList<>();
        mVoltageList = new ArrayList<>();
        mTimeTextList = new ArrayList<>();
        mSdf = new SimpleDateFormat("mm:ss", Locale.CHINA);
    }

    private void loadData(final boolean isPullRefresh) {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                DataFactory.getInstance().getDataSource(true).getEquipmentData(ChargeDetailActivity.this, mEquipmentId, new IHttpRequest.OnResponseListener<Equipment>() {
                    @Override
                    public void onSuccess(final Equipment equipment) {
                        fragments = getFragments(equipment, isPullRefresh);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isInflater) {
                                    isInflater = true;
                                    inflater(mViewStub.inflate());
                                }
                                if (isStartCharge) {
                                    mRingProgress.setProgress(Integer.valueOf(equipment.getPercent()) * 360 / 100);
                                    mPercentTv.setText(equipment.getPercent() + "%");
//                                mTimeTv.setText("预计" + chargeDetailData.mCompleteTime + "后充电完成");

                                    for (int i = 0; i < mLayouts.length; i++) {
                                        View[] vs = mLayoutViewMap.get(mLayouts[i]);
                                        TextView tv1 = (TextView) vs[1];
                                        switch (i) {
                                            case 0:
                                                tv1.setText(equipment.getStartTime() + "小时");
                                                break;
                                            case 1:
                                                tv1.setText(ChargeApplication.getInstance().getUser().getBalance());
                                                break;
                                            case 2:
                                                tv1.setText(equipment.getElectriciryS());
                                                break;
                                        }
                                    }
                                    mViewPager.getAdapter().notifyDataSetChanged();
                                } else {
                                    mRingProgress.setProgress(Integer.valueOf(0 * 360 / 100));
                                    mPercentTv.setText(0 + "%");
//                                mTimeTv.setText("预计" + chargeDetailData.mCompleteTime + "后充电完成");

                                    for (int i = 0; i < mLayouts.length; i++) {
                                        View[] vs = mLayoutViewMap.get(mLayouts[i]);
                                        TextView tv1 = (TextView) vs[1];
                                        switch (i) {
                                            case 0:
                                                tv1.setText(0 + "小时");
                                                break;
                                            case 1:
                                                tv1.setText(ChargeApplication.getInstance().getUser().getBalance());
                                                break;
                                            case 2:
                                                tv1.setText("0");
                                                break;
                                        }
                                    }
                                }
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                });
            }
        });
    }

    private void initFragmentListData(Equipment equipment, boolean isPullRefresh) {
        if (isStartCharge) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - mLastTimeLong > 10000 || isPullRefresh || mCurrentList.size() < 3) {
                mCurrentList.add(equipment.getChargeCurrent());
                mVoltageList.add(equipment.getChargeVoltage());

                int tempCurrentInt = Integer.parseInt(equipment.getChargeCurrent());
                if (tempCurrentInt > mMaxCurrent) {
                    mMaxCurrent = tempCurrentInt;
                } else if (tempCurrentInt < mMinCurrent || mMinCurrent == 0) {
                    mMinCurrent = tempCurrentInt;
                }
                if (mMinCurrent == 0){
                    mMinCurrent = tempCurrentInt;
                }

                int tempVoltageInt = Integer.parseInt(equipment.getChargeVoltage());
                if (tempVoltageInt > mMaxVoltage) {
                    mMaxVoltage = tempVoltageInt;
                } else if (tempVoltageInt < mMinVoltage){
                    mMinVoltage = tempVoltageInt;
                }
                if (mMinVoltage == 0){
                    mMinVoltage = tempVoltageInt;
                }
                mLastTimeLong = currentTimeMillis;
                mTimeTextList.add(mSdf.format(new Date(currentTimeMillis)));
            }
        }

    }

    private Fragment[] getFragments(Equipment equipment, boolean isPullRefresh) {
        initFragmentListData(equipment, isPullRefresh);
        Fragment[] fragments = new Fragment[2];
        fragments[0] = getFragment(mVoltageList, mMinVoltage, mMaxVoltage);
        fragments[1] = getFragment(mCurrentList, mMinCurrent, mMaxCurrent);
        return fragments;
    }

    private Fragment getFragment(List<String> scoreList, int min, int max) {
        Bundle args = new Bundle();
        args.putStringArray(ChargeDetailBaseFragment.MONTH_TEXT, mTimeTextList.toArray(new String[mTimeTextList.size()]));
        args.putStringArray(ChargeDetailBaseFragment.SCORE, scoreList.toArray(new String[scoreList.size()]));
        args.putString(ChargeDetailBaseFragment.MIN_SCORE, String.valueOf(min));
        args.putString(ChargeDetailBaseFragment.MAX_SCORE, String.valueOf(max));
        return ChargeDetailBaseFragment.getInstance(args);
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
        mControlBtn = (Button) root.findViewById(R.id.chargeDetailEndId);
        mControlBtn.setOnClickListener(new View.OnClickListener() {//结束充电
            @Override
            public void onClick(View v) {
                mPd.show();
                if (isStartCharge) {
                    DataFactory.getInstance().getDataSource(true).stopEquipment(ChargeDetailActivity.this, new IHttpRequest.OnResponseListener() {
                        @Override
                        public void onSuccess(Object o) {
                            mPd.dismiss();
                            isStartCharge = false;
                            mControlBtn.setText(ChargeDetailActivity.this.getString(R.string.charge_detail_start_charge));
                            mTimer.cancel();
                        }

                        @Override
                        public void onFail(String errorMsg) {
                            mPd.dismiss();
                            showSnackbar(errorMsg);
                        }
                    });
                } else {
                    DataFactory.getInstance().getDataSource(true).startEquipment(ChargeDetailActivity.this, mEquipmentId, new IHttpRequest.OnResponseListener() {
                        @Override
                        public void onSuccess(Object o) {
                            mPd.dismiss();
                            isStartCharge = true;
                            mControlBtn.setText(ChargeDetailActivity.this.getString(R.string.charge_detail_end_charge));
                            mTimer = new Timer();
                            mTimerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    loadData(false);
                                }
                            };
                            mTimer.schedule(mTimerTask, 10000,10000);
                        }

                        @Override
                        public void onFail(String errorMsg) {
                            mPd.dismiss();
                            showSnackbar(errorMsg);
                            mTimer.purge();
                        }
                    });
                }
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
                    return getString(R.string.charge_detail_voltage);
                else return getString(R.string.charge_detail_current);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
