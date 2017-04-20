package com.gopaychan.constantcharge.charge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gopaychan.constantcharge.R;
import com.gopaychan.constantcharge.base.BaseFragment;
import com.gopaychan.constantcharge.view.ScoreTrend;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.chargeDetailValueTvId)
    TextView mValueTv;
    @Bind(R.id.chargeDetailKeyTvId)
    TextView mKeyTv;

    public static ChargeDetailBaseFragment getInstance(Bundle args) {
        ChargeDetailBaseFragment fragment = new ChargeDetailBaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_charge_detail, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    private void init() {
        Bundle args = getArguments();
        mValueTv.setText(args.getString(VALUE));
        mKeyTv.setText(args.getString(KEY));
        mScoreTrend.setMaxScore(args.getString(MAX_SCORE))
                .setMinScore(args.getString(MIN_SCORE))
                .setScore(args.getStringArray(SCORE))
                .setMonthText(args.getStringArray(MONTH_TEXT)).create();
    }

}
