package com.hengchongkeji.constantcharge.manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class RegisterActivity extends ActionBarActivity {

    @Bind(R.id.registerPhoneNumEdtId)
    EditText mPhoneNumEdt;
    @Bind(R.id.registerInputVerCodeEdtId)
    EditText mVerCodeEdt;
    @Bind(R.id.registerPswEdtId)
    EditText mPswEdt;
    @Bind(R.id.registerInputVerCodeBtnId)
    Button mRegisterVerCodeBtn;
    @Bind(R.id.registerAgreementCbId)
    CheckBox mAgreementCb;
    private String mUserNum;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initView() {
        super.initView();
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRegisterVerCodeBtn.setText("(" + Long.toString(millisUntilFinished / 1000) + "秒)后重试");
                mRegisterVerCodeBtn.setClickable(false);
            }

            @Override
            public void onFinish() {
                mRegisterVerCodeBtn.setText(R.string.register_ver_code_resend);
                mRegisterVerCodeBtn.setClickable(true);
            }
        };
    }

    @OnClick(R.id.registerInputVerCodeBtnId)
    public void getVerCode() {
        getVerCodeInternal();
    }


    private void getVerCodeInternal() {
        mUserNum = mPhoneNumEdt.getText().toString().trim();
        mPhoneNumEdt.clearFocus();
        if (!StringUtils.isMobileNO(mUserNum)) {
            showSnackbar(getString(R.string.register_input_right_phone_num));
            return;
        }
        mPd.show();
        ManagerAction.getVerCode(this, mUserNum, new IHttpRequest.OnResponseListener<String>() {
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

    @OnClick(R.id.registerBtnId)
    public void register() {
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
        if (!mAgreementCb.isChecked()) {
            showSnackbar(getString(R.string.register_no_agree_agreement));
            return;
        }
        mPd.show();
        ManagerAction.register(this, phoneNum, psw, verCode, new IHttpRequest.OnResponseListener<ManagerAction.LoginResponse>() {
            @Override
            public void onSuccess(final ManagerAction.LoginResponse response) {
                showSnackbar(getString(R.string.register_success));
                ChargeApplication.getInstance().saveUserEntity(response.getAppCustomer());
                ManagerAction.login(RegisterActivity.this, phoneNum, psw, new IHttpRequest.OnResponseListener<ManagerAction.LoginResponse>() {
                    @Override
                    public void onSuccess(ManagerAction.LoginResponse response1) {
                        User user = response1.getAppCustomer();
                        user.setPassword(psw);
                        ChargeApplication.getInstance().saveUserEntity(user);
                        mPd.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        ChargeApplication.getInstance().login();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        if (mPd != null && mPd.isShowing()) {
            mPd.dismiss();
        }
    }

    //    private void
}
