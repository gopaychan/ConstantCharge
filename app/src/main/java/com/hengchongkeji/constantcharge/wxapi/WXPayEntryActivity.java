package com.hengchongkeji.constantcharge.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.base.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static android.content.ContentValues.TAG;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    public static final String APP_ID = "wx0a80a24d1aae13c6";
    private IWXAPI mWechatApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWechatApi = WXAPIFactory.createWXAPI(this, APP_ID);
        mWechatApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWechatApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String result = "";
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "成功";

                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "已取消";
                    break;
                default:
                    result = "失败";
                    break;
            }
            Toast.makeText(this,getString(R.string.recharge_pay_result, result),Toast.LENGTH_LONG).show();
            finish();
        }
    }
}