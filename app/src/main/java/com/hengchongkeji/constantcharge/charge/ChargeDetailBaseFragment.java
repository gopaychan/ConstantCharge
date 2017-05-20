package com.hengchongkeji.constantcharge.charge;

import android.os.Bundle;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseFragment;
import com.hengchongkeji.constantcharge.view.ScoreTrend;

import butterknife.Bind;

/**
 * Created by gopayChan on 2017/4/20.
 */

public class ChargeDetailBaseFragment extends BaseFragment {
    public static final String VALUE = "value";
    public static final String KEY = "key";
    public static final String SCORE = "score";
    public static final String MONTH_TEXT = "month_text";
    public static final String MAX_SCORE = "max_score";
    public static final String MIN_SCORE = "min_score";
    @Bind(R.id.chargeDetailScoreTrendId)
    ScoreTrend mScoreTrend;
//    @Bind(R.id.chargeDetailValueTvId)
//    TextView mValueTv;
//    @Bind(R.id.chargeDetailKeyTvId)
//    TextView mKeyTv;

    public static ChargeDetailBaseFragment getInstance(Bundle args) {
        ChargeDetailBaseFragment fragment = new ChargeDetailBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_charge_detail;
    }

    @Override
    protected void postOnCreateView() {
        super.postOnCreateView();
        Bundle args = getArguments();
//        mValueTv.setText(args.getString(VALUE));
//        mKeyTv.setText(args.getString(KEY));
        mScoreTrend.setMaxScore(args.getString(MAX_SCORE))
                .setMinScore(args.getString(MIN_SCORE))
                .setScore(args.getStringArray(SCORE))
                .setMonthText(args.getStringArray(MONTH_TEXT)).create();
    }
}
