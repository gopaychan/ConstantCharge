package com.hengchongkeji.constantcharge.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.DaggerActivityComponent;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.PayResult;
import com.hengchongkeji.constantcharge.executor.ThreadExecutor;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;

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
    @Bind(R.id.rechargeWechatPayReturnTvId)
    TextView mWechatPayReturnTv;
    @Inject
    ThreadExecutor mThreadExecutor;
    View mCharge1View;

    private View mRechargeItemView;
    private IWXAPI mWachatApi;
    private int mCheckIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setTitle(R.string.recharge_title);
        mWachatApi = WXAPIFactory.createWXAPI(this, WXPayEntryActivity.APP_ID);
        mWachatApi.registerApp(WXPayEntryActivity.APP_ID);
        mCharge1View = new View(this);
        mCharge1View.setTag("0.1");
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

    @OnLongClick(R.id.actionBarTitleId)
    public boolean charge1Yuan(){
        if (mRechargeItemView != null && mRechargeItemView != mCharge1View) {
            mRechargeItemView.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_stroke));
        }
        mRechargeItemView = mCharge1View;
        mMoneyTv.setText("合计：¥ " + mRechargeItemView.getTag().toString());
        return true;
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

    @Override
    protected void inject() {
        super.inject();
        DaggerActivityComponent.builder().activityModule(getActivityModule()).applicationComponent(getApplicationComponent()).build().inject(this);
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
                mPd.show();
                ManagerAction.getAliPayInfo(this, price, new IHttpRequest.OnResponseListener<ManagerAction.AliPayResponse>() {
                    @Override
                    public void onSuccess(ManagerAction.AliPayResponse response) {
                        aliPay(response.getData());
                        mPd.dismiss();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        showSnackbar(errorMsg);
                        mPd.dismiss();
                    }
                });
                break;
            case 1:
                mPd.show();
                ManagerAction.getWechatInfo(this, price, new IHttpRequest.OnResponseListener<ManagerAction.WechatResponse>() {
                    @Override
                    public void onSuccess(ManagerAction.WechatResponse o) {
                        mWechatPayReturnTv.setText(o.toString());
                        wechatPay(o);
                        mPd.dismiss();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        showSnackbar(errorMsg);
                        mPd.dismiss();
                    }
                });
                break;
            default:
                showSnackbar(getString(R.string.recharge_choose_pay_way));
                break;
        }
    }

    private void aliPay(final String authInfo) {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AuthTask authTask = new AuthTask(RechargeActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                PayResult payResult = new PayResult(result);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    showSnackbar(getString(R.string.recharge_success));
                } else {
                    showSnackbar(getString(R.string.recharge_fail));
                }
            }
        });
    }

    private void wechatPay(ManagerAction.WechatResponse response) {
        PayReq req = new PayReq();
        req.appId = WXPayEntryActivity.APP_ID;
        req.partnerId = response.getPartnerid();
        req.prepayId = response.getPrepayid();
        req.nonceStr = response.getNonce_str();
        req.timeStamp = response.getTime_start();
        req.packageValue = response.getPackage_();
        req.sign = response.getSign();

        req.extData = "app data"; // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        mWachatApi.sendReq(req);
    }

}
