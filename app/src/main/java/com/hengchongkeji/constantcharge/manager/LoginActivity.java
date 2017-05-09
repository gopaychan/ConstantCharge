package com.hengchongkeji.constantcharge.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.http.IHttpRequest;
import com.hengchongkeji.constantcharge.main.MainActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        super.initView();
        User user = ChargeApplication.getInstance().getUser();
        if (user != null) {
            mPhoneEdt.setText(user.getPhone());
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
        ManagerAction.login(this, phone, psw, new IHttpRequest.OnResponseListener<ManagerAction.LoginResponse>() {
            @Override
            public void onSuccess(ManagerAction.LoginResponse response) {
                mPd.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                ChargeApplication.getInstance().login();
                //登录成功返回个人信息后保存本地
                User user = response.getAppCustomer();
                user.setPassword(psw);
                ChargeApplication.getInstance().saveUserEntity(user);
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

    @OnClick(R.id.showForgetPswActTvId)
    public void forgetPsw(){
        Intent intent = new Intent(this, ForgetPswActivity.class);
        startActivity(intent);
    }
}
