package com.hengchongkeji.constantcharge.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hengchongkeji.constantcharge.ActionBarActivity;
import com.hengchongkeji.constantcharge.ChargeApplication;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by gopayChan on 2017/5/7.
 */

public class ChangePswActivity extends ActionBarActivity {
    @Bind(R.id.actionBarRightTvId)
    TextView mSaveNewPswTv;
    @Bind(R.id.changePswNewPswEdtId)
    EditText mPswEdt;
    @Bind(R.id.changePswConfirmEdtId)
    EditText mConfirmPswEdt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        setTitle("设置密码");
        mSaveNewPswTv.setText("完成");
        mSaveNewPswTv.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.actionBarRightTvId)
    public void savaNewPsw() {
        final String psw = mPswEdt.getText().toString().trim();
        String confirmPsw = mConfirmPswEdt.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            showSnackbar("新密码不能为空");
        } else if (psw.length() < 6 || psw.length() > 11) {
            showSnackbar(getString(R.string.register_psw_invalid));
        } else if (!TextUtils.equals(psw, confirmPsw)) {
            showSnackbar("新密码与确认密码不一致，请重新输入");
        } else {
            final User user = ChargeApplication.getInstance().getUser();
            if (user == null) {
                Toast.makeText(ChangePswActivity.this, "请先登录账号", Toast.LENGTH_LONG).show();
                finish();
            } else {
                ManagerAction.changePsw(this, user.getPhone(), user.getPassword(), psw, new IHttpRequest.OnResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        user.setPassword(psw);
                        ChargeApplication.getInstance().saveUserEntity(user);
                        Toast.makeText(ChangePswActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        showSnackbar(errorMsg);
                    }
                });
            }
        }
    }
}
