package com.hengchongkeji.constantcharge.manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class LoginActivity extends ActionBarActivity {

    @Bind(R.id.loginPhoneNumEdtId)
    EditText mPhoneEdt;
    @Bind(R.id.loginPswEdtId)
    EditText mPswEdt;
    @Bind(R.id.loginBtnId)
    Button mLoginBtn;
    ProgressDialog mPd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPd = new ProgressDialog(this);
    }

    @Override
    protected void initView() {
        super.initView();
        String phone = PreferenceUtils.getUserNumber(this);
        if (!"".equals(phone)) {
            mPhoneEdt.setText(phone);
        }
    }

    @OnClick(R.id.loginBtnId)
    public void login() {
//        if (mPhoneEdt.isFocused())
//            mPhoneEdt.clearFocus();
//        if (mPswEdt.isFocused())
//            mPswEdt.clearFocus();
        mLoginBtn.requestFocus();
        final String phone = mPhoneEdt.getText().toString().trim();
        final String psw = mPswEdt.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(psw)) {
            showSnackbar(getString(R.string.login_input_right_info));
            return;
        }
        mPd.show();
        ManagerAction.login(this, phone, psw, new IHttpRequest.OnResponseListener() {
            @Override
            public void onSuccess(Object o) {
                mPd.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                ChargeApplication.getInstance().login();
                //登录成功返回个人信息后，要存真正的nick
                final String nick = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                PreferenceUtils.saveUserInfo(LoginActivity.this, phone, psw, nick);
                finish();
            }

            @Override
            public void onFail(String errorMsg) {
                mPd.dismiss();
                showSnackbar(errorMsg);
            }
        });
    }

    @OnClick(R.id.showRegisterActTvId)
    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showSnackbar(final String message) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(mPhoneEdt, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
