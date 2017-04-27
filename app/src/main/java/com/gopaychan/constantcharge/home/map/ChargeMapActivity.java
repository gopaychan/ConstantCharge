package com.gopaychan.constantcharge.home.map;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gopaychan.constantcharge.ActionBarActivity;
import com.gopaychan.constantcharge.R;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeMapActivity extends ActionBarActivity {

//    @Bind(R.id.chargeMapDistanceId)
//    LinearLayout mDistanceLl;
//    @Bind(R.id.chargeMapMoneyId)
//    LinearLayout mMoneyLl;
//    @Bind(R.id.chargeMapViewId)
//    MapView mMapView;
//    Map<LinearLayout, View[]> mLayoutViewMap;
//    LinearLayout[] mLayouts;
//
//    View mBubbleLayout;
//    TextView mAddressTv, mFreePileTv, mTotalPileTv, mPredictTvId;
//    Button mDetailBtn, mNavigationBtn;
//
//    @Inject
//    ChargeMapPresenter mPresenter;
//    public static List<Activity> activityList = new ArrayList<>();
//    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void inject() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_map);
        getSupportFragmentManager().beginTransaction().replace(R.id.ChargeMapFragmentFlId,ChargeMapFragment.getInstance());

//        activityList.add(this);
//        mPresenter.start();
//        initPopupWindow();
    }

//    @Override
//    protected void initView() {
//        setTitle(R.string.home_charge_map_txt);
//        mLayouts = new LinearLayout[]{mDistanceLl, mMoneyLl};
//        mLayoutViewMap = new HashMap<>();
//        for (int i = 0; i < mLayouts.length; i++) {
//            View[] views = new View[3];
//            views[0] = mLayouts[i].findViewById(R.id.chargeItemIvId);
//            views[1] = mLayouts[i].findViewById(R.id.chargeItemTv1Id);
//            views[2] = mLayouts[i].findViewById(R.id.chargeItemTv2Id);
//            mLayoutViewMap.put(mLayouts[i], views);
//        }
//        for (int i = 0; i < mLayouts.length; i++) {
//            View[] vs = mLayoutViewMap.get(mLayouts[i]);
//            ImageView iv = (ImageView) vs[0];
//            TextView tv1 = (TextView) vs[1];
//            TextView tv2 = (TextView) vs[2];
//
//            switch (i) {
//                case 0:
//                    tv2.setText(getString(R.string.charge_map_distance));
//                    iv.setBackgroundResource(R.drawable.charge_detail_time);
//                    tv1.setText("5000米");
//                    break;
//                case 1:
//                    tv2.setText(getString(R.string.charge_map_money));
//                    iv.setBackgroundResource(R.drawable.charge_detail_money);
//                    break;
//            }
//        }
//    }
//
//    private void initPopupWindow() {
//        mBubbleLayout = LayoutInflater.from(this).inflate(R.layout.popup_charge_map, null);
//        mAddressTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapAddressTvId);
//        mTotalPileTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapTotalTvId);
//        mFreePileTv = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapFreeTvId);
//        mPredictTvId = (TextView) mBubbleLayout.findViewById(R.id.popupChargeMapPredictTvId);
//        mDetailBtn = (Button) mBubbleLayout.findViewById(R.id.popupChargeMapDetailBtnId);
//        mNavigationBtn = (Button) mBubbleLayout.findViewById(R.id.popupChargeMapnavigationBtnId);
//        mNavigationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected void inject() {
//        DaggerActivityComponent.builder().applicationComponent(getApplicationComponent()).activityModule(getActivityModule()).build().inject(this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mPresenter.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mPresenter.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestroy();
//    }
}
