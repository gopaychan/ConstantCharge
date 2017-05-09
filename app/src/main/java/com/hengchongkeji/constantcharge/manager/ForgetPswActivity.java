package com.hengchongkeji.constantcharge.manager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/5/7.
 */

public class ForgetPswActivity extends ActionBarActivity {
    @Bind(R.id.forgetPswEdtId)
    EditText mPswEdt;
    @Bind(R.id.forgetPswInputVerCodeEdtId)
    EditText mVerCodeEdt;
    @Bind(R.id.forgetPswPhoneNumEdtId)
    EditText mPhoneNumEdt;
    @Bind(R.id.forgetPswInputVerCodeBtnId)
    Button mVerCodeBtnId;
    private String mUserNum;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
    }

    @OnClick(R.id.forgetPswInputVerCodeBtnId)
    public void getVerCode() {
        getVerCodeInternal();
    }

    @Override
    protected void initView() {
        super.initView();
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mVerCodeBtnId.setText("(" + Long.toString(millisUntilFinished / 1000) + "秒)后重试");
                mVerCodeBtnId.setClickable(false);
            }

            @Override
            public void onFinish() {
                mVerCodeBtnId.setText(R.string.register_ver_code_resend);
                mVerCodeBtnId.setClickable(true);
            }
        };
    }

    private void getVerCodeInternal() {
        mUserNum = mPhoneNumEdt.getText().toString().trim();
        mPhoneNumEdt.clearFocus();
        if (!StringUtils.isMobileNO(mUserNum)) {
            showSnackbar(getString(R.string.register_input_right_phone_num));
            return;
        }
        mPd.show();
        ManagerAction.forgetPswGetVerCode(this, mUserNum, new IHttpRequest.OnResponseListener<String>() {
            @Override
            public void onSuccess(String o) {
                mPd.dismiss();
                showSnackbar(o);
                mTimer.start();
            }

            @Override
            public void onFail(String errorMsg) {
                mPd.dismiss();
                showSnackbar(errorMsg);
            }
        });
    }

    @OnClick(R.id.forgetPswBtnId)
    public void forgetPswChange() {
        final String phoneNum = mPhoneNumEdt.getText().toString().trim();
        String verCode = mVerCodeEdt.getText().toString().trim();
        final String psw = mPswEdt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showSnackbar(getString(R.string.register_phone_no_null));
            return;
        }
        if (!phoneNum.equals(mUserNum)) {
            showSnackbar(getString(R.string.register_phone_num_error));
            return;
        }
        if (verCode.length() != 6) {
            showSnackbar(getResources().getString(R.string.register_ver_code_error));
            return;
        }
        if (psw.length() < 6 || psw.length() > 11) {
            showSnackbar(getString(R.string.register_psw_invalid));
            return;
        }
        mPd.show();
        ManagerAction.forgetPswCheckVerCode(this, phoneNum, verCode, new IHttpRequest.OnResponseListener() {
            @Override
            public void onSuccess(Object o) {
                ManagerAction.forgetPswChangePsw(ForgetPswActivity.this, psw, psw, new IHttpRequest.OnResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        mPd.dismiss();
                        Toast.makeText(ForgetPswActivity.this, o.toString(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        mPd.dismiss();
                        showSnackbar(errorMsg);
                    }
                });
            }

            @Override
            public void onFail(String errorMsg) {
                mPd.dismiss();
                showSnackbar(errorMsg);
            }
        });

    }
}
