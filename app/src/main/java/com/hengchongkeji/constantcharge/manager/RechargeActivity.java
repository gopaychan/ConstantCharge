package com.hengchongkeji.constantcharge.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class RechargeActivity extends ActionBarActivity {

    @Bind(R.id.rechargeAliPayCbId)
    CheckBox mAliPayCb;
    @Bind(R.id.rechargeWechatPayCbId)
    CheckBox mWechatPayCb;
    @Bind(R.id.rechargeMoneyTvId)
    TextView mMoneyTv;

    private View mRechargeItemView;
    private int mCheckIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setTitle(R.string.recharge_title);
    }

    @OnClick(R.id.rechargeAliPayRlId)
    public void checkAliPay() {
        switch (mCheckIndex) {
            case 0:
                break;
            case 1:
                mWechatPayCb.setChecked(false);
                mCheckIndex = 0;
                mAliPayCb.setChecked(true);
                break;
            default:
                mCheckIndex = 0;
                mAliPayCb.setChecked(true);
                break;
        }

    }

    @OnClick(R.id.rechargeWechatPayRlId)
    public void checkWechatPay() {
        switch (mCheckIndex) {
            case 0:
                mAliPayCb.setChecked(false);
                mCheckIndex = 1;
                mWechatPayCb.setChecked(true);
                break;
            case 1:
                break;
            default:
                mCheckIndex = 1;
                mWechatPayCb.setChecked(true);
                break;
        }

    }

    public void onItemClick(View v) {
        if (mRechargeItemView != null) {
            if (mRechargeItemView != v) {
                mRechargeItemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_stroke));
                mRechargeItemView = v;
                mRechargeItemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.theme_stroke));
                mMoneyTv.setText("合计：¥ " + mRechargeItemView.getTag().toString());
            }
        } else {
            mRechargeItemView = v;
            mRechargeItemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.theme_stroke));
            mMoneyTv.setText("合计：¥ " + mRechargeItemView.getTag().toString());
        }

    }

    @OnClick(R.id.rechargeTvId)
    public void recharge() {
        String price;
        if (mRechargeItemView == null) {
            showSnackbar(getString(R.string.recharge_choose_price));
            return;
        } else {
            price = mRechargeItemView.getTag().toString();
        }

        switch (mCheckIndex) {
            case 0:
                showSnackbar("选择了支付宝，充值" + price + "元");
                break;
            case 1:
                showSnackbar("选择了微信 ，充值" + price + "元");
                break;
            default:
                showSnackbar(getString(R.string.recharge_choose_pay_way));
                break;
        }
    }

}
